package com.esflink.starter.properties;


import com.esflink.starter.configuration.FlinkJobPropertiesConfiguration;
import com.ververica.cdc.connectors.mysql.table.StartupMode;
import com.ververica.cdc.connectors.mysql.table.StartupOptions;

/**
 * flink job 配置信息
 *
 * @author zhouhongyin
 * @see FlinkJobPropertiesConfiguration
 * @since 2023/3/5 22:28
 */
public class FlinkJobProperties {
    private String name;

    private String hostname;

    private String port;

    private String databaseList;

    private String tableList;

    private String username;

    private String password;

    /**
     * <p>
     * MySQL CDC使用者的启动模式
     * </p>
     *
     * <pre>
     * INITIAL: 初始化快照,即全量导入后增量导入(检测更新数据写入)
     * LATEST: 只进行增量导入(不读取历史变化)
     * TIMESTAMP: 指定时间戳进行数据导入(大于等于指定时间错读取数据)
     * </pre>
     *
     * @see StartupMode
     */
    private String startupMode;

    private Long startupTimestampMillis;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabaseList() {
        return databaseList;
    }

    public void setDatabaseList(String databaseList) {
        this.databaseList = databaseList;
    }

    public String getTableList() {
        return tableList;
    }

    public void setTableList(String tableList) {
        this.tableList = tableList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStartupMode() {
        return startupMode;
    }

    public void setStartupMode(String startupMode) {
        this.startupMode = startupMode;
    }

    public Long getStartupTimestampMillis() {
        return startupTimestampMillis;
    }

    public void setStartupTimestampMillis(Long startupTimestampMillis) {
        this.startupTimestampMillis = startupTimestampMillis;
    }

    public StartupOptions getStartupOptions() {
        String startupMode = this.startupMode;
        if ("INITIAL".equals(startupMode)) return StartupOptions.initial();
        if ("LATEST".equals(startupMode)) return StartupOptions.latest();
        if ("TIMESTAMP".equals(startupMode)) return StartupOptions.timestamp(startupTimestampMillis);
        return StartupOptions.latest();
    }
}
