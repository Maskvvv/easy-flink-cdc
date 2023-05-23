package com.esflink.starter.config;


import com.esflink.starter.constants.BaseEsConstants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * flink 数据库配置信息
 *
 * @author zhouhongyin
 * @since 2023/3/5 22:28
 */
@Configuration
@ConfigurationProperties(value = "easy-flink")
@ConditionalOnProperty(name = BaseEsConstants.ENABLE_PREFIX, havingValue = "true", matchIfMissing = true)
public class FlinkProperties {
    private String name;

    private String hostname;

    private Integer port;

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

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
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


    public static final class FlinkPropertiesBuilder {
        private String name;
        private String hostname;
        private Integer port;
        private String databaseList;
        private String tableList;
        private String username;
        private String password;

        private FlinkPropertiesBuilder() {
        }

        public static FlinkPropertiesBuilder aFlinkProperties() {
            return new FlinkPropertiesBuilder();
        }

        public FlinkPropertiesBuilder name(String name) {
            this.name = name;
            return this;
        }

        public FlinkPropertiesBuilder hostname(String hostname) {
            this.hostname = hostname;
            return this;
        }

        public FlinkPropertiesBuilder port(Integer port) {
            this.port = port;
            return this;
        }

        public FlinkPropertiesBuilder databaseList(String databaseList) {
            this.databaseList = databaseList;
            return this;
        }

        public FlinkPropertiesBuilder tableList(String tableList) {
            this.tableList = tableList;
            return this;
        }

        public FlinkPropertiesBuilder username(String username) {
            this.username = username;
            return this;
        }

        public FlinkPropertiesBuilder password(String password) {
            this.password = password;
            return this;
        }

        public FlinkProperties build() {
            FlinkProperties flinkProperties = new FlinkProperties();
            flinkProperties.setName(name);
            flinkProperties.setHostname(hostname);
            flinkProperties.setPort(port);
            flinkProperties.setDatabaseList(databaseList);
            flinkProperties.setTableList(tableList);
            flinkProperties.setUsername(username);
            flinkProperties.setPassword(password);
            return flinkProperties;
        }
    }
}
