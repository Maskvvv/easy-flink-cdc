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

    private String databaseList;

    private String tableList;

    private String username;

    private String password;


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


}
