package com.esflink.starter.meta;

import com.ververica.cdc.connectors.mysql.table.StartupOptions;

/**
 * mysql binlog读取位置
 *
 * @see StartupOptions#timestamp(long)
 */
public class LogPosition {

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
