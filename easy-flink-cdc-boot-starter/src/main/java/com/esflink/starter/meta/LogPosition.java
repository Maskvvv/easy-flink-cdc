package com.esflink.starter.meta;

import java.io.Serializable;

/**
 * mysql binlog读取位置
 *
 * @see StartupOptions#timestamp(long)
 */
public class LogPosition implements Serializable {

    private FlinkJobIdentity flinkJobIdentity;


    private Long startupTimestampMillis;

    public FlinkJobIdentity getFlinkJobIdentity() {
        return flinkJobIdentity;
    }

    public void setFlinkJobIdentity(FlinkJobIdentity flinkJobIdentity) {
        this.flinkJobIdentity = flinkJobIdentity;
    }

    public Long getStartupTimestampMillis() {
        return startupTimestampMillis;
    }

    public void setStartupTimestampMillis(Long startupTimestampMillis) {
        this.startupTimestampMillis = startupTimestampMillis;
    }
}
