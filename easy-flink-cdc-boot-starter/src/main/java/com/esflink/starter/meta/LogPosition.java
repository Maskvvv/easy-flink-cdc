package com.esflink.starter.meta;

import com.ververica.cdc.connectors.mysql.table.StartupOptions;

/**
 * mysql binlog读取位置
 *
 * @see StartupOptions#timestamp(long)
 */
public class LogPosition {

    private FlinkJobIdentity applicationName;


    private Long startupTimestampMillis;

    public FlinkJobIdentity getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(FlinkJobIdentity applicationName) {
        this.applicationName = applicationName;
    }

    public Long getStartupTimestampMillis() {
        return startupTimestampMillis;
    }

    public void setStartupTimestampMillis(Long startupTimestampMillis) {
        this.startupTimestampMillis = startupTimestampMillis;
    }
}
