package com.esflink.starter.properties;


import com.esflink.starter.configuration.FlinkJobPropertiesConfiguration;

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

    /**
     * 数据库类型：MSSQL、MYSQL
     */
    private String dbType;

    /**
     * <p>数据库名</p>
     * <p>example: database1,database2</p>
     *
     * @see MySqlSource.Builder#databaseList
     */
    private String databaseList;

    /**
     * <p>数据库表名</p>
     * <p>example: database1.table1,database2.table2</p>
     *
     * @see MySqlSource.Builder#tableList
     */
    private String tableList;

    private String username;

    private String password;

    /**
     * @see MySqlSource.Builder#serverTimeZone
     */
    private String serverTimeZone = "GMT+8";

    /**
     * <p>
     * MySQL CDC使用者的启动模式
     * </p>
     *
     * <pre>
     *     <li>INITIAL: 初始化快照,即全量导入后增量导入(检测更新数据写入)</li>
     *     <li>LATEST: 只进行增量导入(不读取历史变化)</li>
     *     <li>TIMESTAMP: 指定时间戳进行数据导入(大于等于指定时间错读取数据)</li>
     * </pre>
     *
     * @see StartupMode
     */
    private String startupMode;

    private Long startupTimestampMillis;

    /**
     * serverId
     *
     * @see MySqlSource.Builder#serverId
     */
    private Integer serverId;

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

    public String getServerTimeZone() {
        return serverTimeZone;
    }

    public void setServerTimeZone(String serverTimeZone) {
        this.serverTimeZone = serverTimeZone;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public org.apache.flink.cdc.connectors.mysql.table.StartupOptions getMysqlStartupOptions() {
        String startupMode = this.startupMode;

        switch (startupMode.toUpperCase()) {
            case "INITIAL":
                return org.apache.flink.cdc.connectors.mysql.table.StartupOptions.initial();
            case "TIMESTAMP":
                return org.apache.flink.cdc.connectors.mysql.table.StartupOptions.timestamp(startupTimestampMillis);
            default:
                return org.apache.flink.cdc.connectors.mysql.table.StartupOptions.latest();
        }
    }

    public org.apache.flink.cdc.connectors.base.options.StartupOptions getMssqlStartupOptions() {
        String startupMode = this.startupMode;

        switch (startupMode.toUpperCase()) {
            case "INITIAL":
                return org.apache.flink.cdc.connectors.base.options.StartupOptions.initial();
            case "LATEST":
            default:
                return org.apache.flink.cdc.connectors.base.options.StartupOptions.latest();
        }
    }
}
