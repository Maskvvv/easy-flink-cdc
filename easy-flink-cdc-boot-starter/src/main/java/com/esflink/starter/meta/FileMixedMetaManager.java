package com.esflink.starter.meta;


import com.alibaba.fastjson.JSON;
import com.esflink.starter.common.exception.MetaManagerException;
import com.esflink.starter.common.utils.LogUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.PathMatcher;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * 基于文件刷新的metaManager实现
 *
 * <pre>
 * 策略：
 * 1. 先写内存，然后定时刷新数据到File
 * 2. 数据采取overwrite模式(只保留最后一次)
 * </pre>
 *
 * @author zhouhongyin
 * @version 1.0.4
 */
@Component
public class FileMixedMetaManager extends MemoryMetaManager implements MetaManager {

    private static final Logger logger = Logger.getLogger(FileMixedMetaManager.class.getName());


    private static final Charset charset = StandardCharsets.UTF_8;
    /**
     * 游标文件存放路径
     */
    private File dataDir;
    private String dataFileName = "meta.dat";

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

        dataFileCaches = new ConcurrentHashMap<>();

        updateCursorTasks = new CopyOnWriteArraySet<>();

        loadCursor();

        executor = Executors.newScheduledThreadPool(1);
        // 启动定时工作任务
        executor.scheduleAtFixedRate(() -> {
                    List<FlinkJobIdentity> tasks = new ArrayList<>(updateCursorTasks);
                    for (FlinkJobIdentity flinkJobIdentity : tasks) {
                        try {
                            updateCursorTasks.remove(flinkJobIdentity);

                            // 定时将内存中的最新值刷到file中，多次变更只刷一次
                            flushDataToFile(flinkJobIdentity);
                        } catch (Throwable e) {
                            // ignore
                            LogUtils.error("period update [" + flinkJobIdentity.toString() + "] curosr failed!", e.getMessage());
                        }
                    }
                },
                period,
                period,
                TimeUnit.MILLISECONDS);
    }

    public void stop() {
        flushDataToFile();// 刷新数据

        super.stop();
        executor.shutdownNow();
        destinations.clear();
        batches.clear();
    }


    private void flushDataToFile() {
        for (String destination : destinations.keySet()) {
            flushDataToFile(destination);
        }
    }

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


    private void loadCursor() throws IOException {
        PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver(new FileSystemResourceLoader());
        resourcePatternResolver.setPathMatcher(new PathMatcher() {
            @Override
            public boolean isPattern(String path) {
                return false;
            }

            @Override
            public boolean match(String pattern, String path) {
                return false;
            }

            @Override
            public boolean matchStart(String pattern, String path) {
                return false;
            }

            @Override
            public String extractPathWithinPattern(String pattern, String path) {
                return null;
            }

            @Override
            public Map<String, String> extractUriTemplateVariables(String pattern, String path) {
                return null;
            }

            @Override
            public Comparator<String> getPatternComparator(String path) {
                return null;
            }

            @Override
            public String combine(String pattern1, String pattern2) {
                return null;
            }
        });
        Resource[] resources = resourcePatternResolver.getResources("file*:" + dataDir.getAbsolutePath());

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
        this.dataDir = new File(dataDir);
    }

    public void setDataDirByFile(File dataDir) {
        this.dataDir = dataDir;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

}
