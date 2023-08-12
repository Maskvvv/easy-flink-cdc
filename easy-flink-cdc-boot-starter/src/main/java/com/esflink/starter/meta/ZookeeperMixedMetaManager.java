package com.esflink.starter.meta;


import com.alibaba.fastjson.JSON;
import com.esflink.starter.common.utils.LogUtils;
import com.esflink.starter.holder.FlinkJobPropertiesHolder;
import com.esflink.starter.properties.EasyFlinkProperties;
import com.esflink.starter.properties.FlinkJobProperties;
import com.esflink.starter.zookeeper.FlinkJobZKPathUtils;
import com.esflink.starter.zookeeper.ZkClientx;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
import java.util.logging.Logger;

/**
 * 基于 zookeeper 刷新的 metaManager 实现
 *
 * <h2>写入策略</h2>
 * <pre>
 * 1. 先写内存，然后定时刷新数据到 zookeeper
 * 2. 数据采取overwrite模式(只保留最后一次)
 * </pre>
 *
 * <h2>存储结构</h2>
 * <pre>
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
public class ZookeeperMixedMetaManager extends MemoryMetaManager implements MetaManager, Serializable {

    private static final Logger logger = Logger.getLogger(ZookeeperMixedMetaManager.class.getName());
    private static final Charset charset = StandardCharsets.UTF_8;
    public static final String NAME = "zookeeper";

    @Autowired
    private EasyFlinkProperties easyFlinkProperties;

    @Autowired
    private ZkClientx zkClientx;

    /**
     * <p>flinkJob zookeeper path map</p>
     */
    private Map<FlinkJobIdentity, String> identifyZKPathMap;

    private long period = 1000;

    private ScheduledExecutorService executor;

    /**
     * 待记录的游标任务
     */
    private Set<FlinkJobIdentity> updateCursorTasks;

    public void start() {
        super.start();

        initIdentifyZKPathMap();

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
                            flushDataToZookeeper(flinkJobIdentity);
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

    @Override
    public void updateCursor(FlinkJobIdentity flinkJobIdentity, LogPosition position) {
        super.updateCursor(flinkJobIdentity, position);
        updateCursorTasks.add(flinkJobIdentity);
    }

    /**
     * <p>启动时为每个 FlinkJob 创建 zookeeper 的 path 映射</p>
     */
    private void initIdentifyZKPathMap() {
        identifyZKPathMap = new ConcurrentHashMap<>();
        List<FlinkJobProperties> properties = FlinkJobPropertiesHolder.getProperties();
        if (CollectionUtils.isEmpty(properties)) return;

        for (FlinkJobProperties property : properties) {
            EasyFlinkProperties.Meta meta = easyFlinkProperties.getMeta();
            FlinkJobIdentity flinkJobIdentity = FlinkJobIdentity.generate(meta, property.getName());
            String zkPath = FlinkJobZKPathUtils.create(flinkJobIdentity);
            identifyZKPathMap.put(flinkJobIdentity, zkPath);
        }
    }

    public void stop() {
        flushDataToZookeeper();// 刷新数据
        super.stop();
        executor.shutdownNow();
    }


    private void flushDataToZookeeper() {
        for (FlinkJobIdentity flinkJobIdentity : cursors.keySet()) {
            flushDataToZookeeper(flinkJobIdentity);
        }
    }

    /**
     * <p>将内存中的 FlinkJob cursor 持久化</p>
     *
     * @param flinkJobIdentity FlinkJob 唯一标识
     */
    private void flushDataToZookeeper(FlinkJobIdentity flinkJobIdentity) {
        flushDataToZookeeper(flinkJobIdentity, identifyZKPathMap.get(flinkJobIdentity));
    }

    private void flushDataToZookeeper(FlinkJobIdentity flinkJobIdentity, String zkPath) {

        LogPosition logPosition = this.cursors.get(flinkJobIdentity);
        String json = JSON.toJSONString(logPosition);

        try {
            zkClientx.writeData(zkPath, json);
        } catch (ZkNoNodeException e) {
            zkClientx.createPersistent(zkPath, json, true);
        }
    }

    /**
     * <p>启动时加载 zookeeper 中的 cursor 到内存，
     * 使得 FlinkJob 可以接着上次 cursor 继续监听 binlog
     * </p>
     */
    private void loadCursor() {
        for (Map.Entry<FlinkJobIdentity, String> flinkJobIdentityEntry : identifyZKPathMap.entrySet()) {
            String path = flinkJobIdentityEntry.getValue();
            try {
                String cursor = zkClientx.readDataString(path);
                LogPosition logPosition = JSON.parseObject(cursor, LogPosition.class);
                this.cursors.put(logPosition.getFlinkJobIdentity(), logPosition);
            } catch (Exception e) {
                zkClientx.createPersistent(path, true);
            }

        }
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
