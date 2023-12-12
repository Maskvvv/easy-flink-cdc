package com.esflink.starter.properties;


import com.esflink.starter.configuration.MetaManagerConfiguration;
import com.esflink.starter.constants.BaseEsConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.Serializable;

/**
 * flink 数据库配置信息
 *
 * @author zhouhongyin
 * @since 2023/3/5 22:28
 */
@EnableConfigurationProperties(EasyFlinkProperties.class)
@ConfigurationProperties(value = BaseEsConstants.PREFIX)
@ConditionalOnProperty(name = BaseEsConstants.ENABLE_PREFIX, havingValue = "true", matchIfMissing = false)
public class EasyFlinkProperties implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(EasyFlinkProperties.class);

    private String enable;

    private Nacos cloudConfig;

    /**
     * <p> Cursor 的记录方式 </p>
     *
     * <ul>
     *  <li>file</li>
     *  <li>memory</li>
     *  <li>zookeeper</li>
     * <ul/>
     *
     * @see MetaManagerConfiguration
     */
    private String metaModel;

    private Meta meta;

    private Zookeeper zookeeper;

    @Autowired
    @JsonIgnore
    private Environment environment;

    @PostConstruct
    public void init() {
        this.overrideFromEnv();
    }

    private void overrideFromEnv() {
        if (this.meta == null) {
            this.meta = new Meta();
        }

        if (StringUtils.isBlank(meta.getApplicationName())) {
            String applicationName = environment.resolvePlaceholders("${spring.application.name:unname}");
            this.meta.setApplicationName(applicationName);
        }

        if (StringUtils.isBlank(meta.getPort())) {
            String port = environment.resolvePlaceholders("${server.port:unport}");
            this.meta.setPort(port);
        }

        if (StringUtils.isBlank(meta.getDataDir())) {
            String dataDir = environment.resolvePlaceholders("${user.home}");
            this.meta.setDataDir(dataDir);
        }

        if (meta.getDataDir().endsWith(File.separator)) {
            meta.setDataDir(meta.getDataDir().substring(0, meta.getDataDir().length() - 1));
        }

        logger.info("meta dataDir is: {}", meta.getDataDir());
    }


    public static class Nacos {
        public static final String DEFAULT_GROUP = "DEFAULT_GROUP";

        private String enable;

        private Position position;

        public String getEnable() {
            return enable;
        }

        public void setEnable(String enable) {
            this.enable = enable;
        }

        public Position getPosition() {
            return position;
        }

        public void setPosition(Position position) {
            this.position = position;
        }

        public static class Position {
            private String dataId;
            private String group = DEFAULT_GROUP;

            public String getDataId() {
                return dataId;
            }

            public void setDataId(String dataId) {
                this.dataId = dataId;
            }

            public String getGroup() {
                return group;
            }

            public void setGroup(String group) {
                this.group = group;
            }

        }
    }

    public static class Zookeeper {
        private String address;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

    public static class Meta {

        private String applicationName;

        private String port;

        private String dataDir;

        public String getDataDir() {
            return dataDir;
        }

        public void setDataDir(String dataDir) {
            this.dataDir = dataDir;
        }

        public String getApplicationName() {
            return applicationName;
        }

        public void setApplicationName(String applicationName) {
            this.applicationName = applicationName;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }
    }

    public Zookeeper getZookeeper() {
        return zookeeper;
    }

    public void setZookeeper(Zookeeper zookeeper) {
        this.zookeeper = zookeeper;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public Nacos getCloudConfig() {
        return cloudConfig;
    }

    public void setCloudConfig(Nacos cloudConfig) {
        this.cloudConfig = cloudConfig;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public String getMetaModel() {
        return metaModel;
    }

    public void setMetaModel(String metaModel) {
        this.metaModel = metaModel;
    }
}
