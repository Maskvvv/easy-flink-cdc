package com.esflink.starter.meta;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存版实现
 *
 * @author zhouhongyin
 * @version 1.0.0
 */
public class MemoryMetaManager extends AbstractMetaLifeCycle implements MetaManager {
    protected Map<FlinkJobIdentity, LogPosition> cursors;
    protected Map<String, FlinkJobIdentity> flinkJobs;

    public void start() {
        super.start();
        cursors = new ConcurrentHashMap<>();
        flinkJobs = new ConcurrentHashMap<>();
    }

    public void stop() {
        super.stop();
        cursors.clear();
        flinkJobs.clear();
    }


    @Override
    public LogPosition getCursor(FlinkJobIdentity flinkJobIdentity) {
        return cursors.get(flinkJobIdentity);
    }

    @Override
    public void updateCursor(FlinkJobIdentity flinkJobIdentity, LogPosition position) {
        cursors.put(flinkJobIdentity, position);
    }
}
