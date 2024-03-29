package com.esflink.starter.meta;


import com.alibaba.fastjson.JSON;
import com.esflink.starter.common.exception.MetaManagerException;
import com.esflink.starter.common.utils.ResourceUtils;
import com.esflink.starter.holder.FlinkJobPropertiesHolder;
import com.esflink.starter.properties.EasyFlinkProperties;
import com.esflink.starter.properties.FlinkJobProperties;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 基于文件刷新的 metaManager 实现
 *
 * <h2>写入策略</h2>
 * <pre>
 * 1. 先写内存，然后定时刷新数据到File
 * 2. 数据采取overwrite模式(只保留最后一次)
 * </pre>
 *
 * <h2>存储结构</h2>
 * <pre>
 * user.home
 *   easy-flink-cdc
 *     applicationName port
 *       flinkJobName1
 *         meta.dat
 *           sink1
 *           sink2
 *       flinkJobName2
 *         meta.dat
 *           sink1
 *           sink2
 * </pre>
 *
 * @author zhouhongyin
 * @version 1.0.4
 */
@Component
public class FileMixedMetaManager extends MemoryMetaManager implements MetaManager, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(FileMixedMetaManager.class);
    private static final Charset charset = StandardCharsets.UTF_8;
    private final String NAME = "file";

    @Autowired
    private EasyFlinkProperties easyFlinkProperties;

    /**
     * 游标文件存放路径
     */
    private File dataDir;
    private String dataFilePrefix = "easy-flink-cdc";
    private String dataFileName = "meta.dat";
    private static final String dataDirEndsWith = "/**/meta.dat";

    /**
     * flinkJob 游标文件map
     */
    private Map<FlinkJobIdentity, File> dataFileCaches;

    private long period = 1000;

    private ScheduledExecutorService executor;

    /**
     * 待记录的游标任务
     */
    private Set<FlinkJobIdentity> updateCursorTasks;

    public void start() {
        super.start();
        // 设置 meta.dat 存放位置
        EasyFlinkProperties.Meta meta = easyFlinkProperties.getMeta();
        setDataDir(meta.getDataDir());

        if (!dataDir.exists()) {
            try {
                FileUtils.forceMkdir(dataDir);
            } catch (IOException e) {
                throw new MetaManagerException(e);
            }
        }

        if (!dataDir.canRead() || !dataDir.canWrite()) {
            throw new MetaManagerException("dir[" + dataDir.getPath() + "] can not read/write");
        }

        initDataFileCaches();

        updateCursorTasks = new CopyOnWriteArraySet<>();
        loadCursor();

        executor = Executors.newScheduledThreadPool(1, new CustomizableThreadFactory("easy-flink-cdc-meta-file-"));
        // 启动定时工作任务
        executor.scheduleAtFixedRate(() -> {
                    List<FlinkJobIdentity> tasks = new ArrayList<>(updateCursorTasks);
                    for (FlinkJobIdentity flinkJobIdentity : tasks) {
                        try {
                            updateCursorTasks.remove(flinkJobIdentity);

                            // 定时将内存中的最新值刷到file中，多次变更只刷一次
                            flushDataToFile(flinkJobIdentity);
                        } catch (Throwable e) {
                            logger.error("period update [{}] curosr failed!", flinkJobIdentity, e);
                        }
                    }
                },
                period,
                period,
                TimeUnit.MILLISECONDS);
    }

    @Override
    public void updateCursor(FlinkJobIdentity flinkJobIdentity, LogPosition position) {
        super.updateCursor(flinkJobIdentity, position);
        updateCursorTasks.add(flinkJobIdentity);
    }

    /**
     * <p>启动时为每个 FlinkJob 创建 meta.dat 的存放路径</p>
     */
    private void initDataFileCaches() {
        dataFileCaches = new ConcurrentHashMap<>();
        List<FlinkJobProperties> properties = FlinkJobPropertiesHolder.getProperties();
        if (CollectionUtils.isEmpty(properties)) return;

        for (FlinkJobProperties property : properties) {
            EasyFlinkProperties.Meta meta = easyFlinkProperties.getMeta();
            FlinkJobIdentity flinkJobIdentity = FlinkJobIdentity.generate(meta, property.getName());
            File file = new File(dataDir, File.separator + flinkJobIdentity.getApplicationName() + flinkJobIdentity.getPort() + File.separator + flinkJobIdentity.getFlinkJobName() + File.separator + dataFileName);
            dataFileCaches.put(flinkJobIdentity, file);
        }
    }

    public void stop() {
        flushDataToFile();// 刷新数据
        super.stop();
        executor.shutdownNow();
    }


    private void flushDataToFile() {
        for (FlinkJobIdentity flinkJobIdentity : cursors.keySet()) {
            flushDataToFile(flinkJobIdentity);
        }
    }

    /**
     * <p>将内存中的 FlinkJob cursor 持久化</p>
     *
     * @param flinkJobIdentity FlinkJob 唯一标识
     */
    private void flushDataToFile(FlinkJobIdentity flinkJobIdentity) {
        flushDataToFile(flinkJobIdentity, dataFileCaches.get(flinkJobIdentity));
    }

    private void flushDataToFile(FlinkJobIdentity flinkJobIdentity, File dataFile) {

        LogPosition logPosition = this.cursors.get(flinkJobIdentity);
        String json = JSON.toJSONString(logPosition);
        try {
            FileUtils.writeStringToFile(dataFile, json, charset);
        } catch (IOException e) {
            throw new MetaManagerException(e);
        }

    }

    /**
     * <p>启动时加载 {@code dataDir} 路径下的 cursor 到内存，
     * 使得 FlinkJob 可以接着上次 cursor 继续监听 binlog
     * </p>
     */
    private void loadCursor() {
        PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver(new FileSystemResourceLoader());
        try {
            Resource[] resources = resourcePatternResolver.getResources("file:" + dataDir.getPath() + dataDirEndsWith);
            if (ObjectUtils.isEmpty(resources)) return;

            for (Resource resource : resources) {
                String content = ResourceUtils.getContent(resource);

                LogPosition logPosition = JSON.parseObject(content, LogPosition.class);
                this.cursors.put(logPosition.getFlinkJobIdentity(), logPosition);
            }

        } catch (IOException e) {
            throw new MetaManagerException(e);
        }
    }

    private File getDataFile(String flinkJobName) {
        File destinationMetaDir = new File(dataDir, flinkJobName);
        if (!destinationMetaDir.exists()) {
            try {
                FileUtils.forceMkdir(destinationMetaDir);
            } catch (IOException e) {
                throw new MetaManagerException(e);
            }
        }

        return new File(destinationMetaDir, dataFileName);
    }


    public void setDataDir(String dataDir) {
        dataDir = dataDir + File.separator + dataFilePrefix;
        this.dataDir = new File(dataDir);
    }

    public void setDataDirByFile(File dataDir) {
        this.dataDir = dataDir;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
