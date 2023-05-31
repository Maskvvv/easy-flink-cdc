package com.esflink.starter.meta;

import com.ververica.cdc.connectors.mysql.table.StartupOptions;

/**
 * mysql binlog读取位置
 *
 * @see StartupOptions#timestamp(long)
 */
public class LogPosition {

    private String applicationName;


    private long startupTimestampMillis;


}
