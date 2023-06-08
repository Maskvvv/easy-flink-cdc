package com.esflink.starter.properties;


import com.esflink.starter.constants.BaseEsConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
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
@ConditionalOnProperty(name = BaseEsConstants.ENABLE_PREFIX, havingValue = "true", matchIfMissing = true)
public class EasyFlinkProperties implements Serializable {

    private String enable;

    private Nacos cloudConfig;

    private MetaModel metaModel = MetaModel.FILE;

    private Meta meta;

    private ZkClientx zkClientx;

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
    }


    public static class Nacos {
        public static final String DEFAULT_GROUP = "DEFAULT_GROUP";

        private Position position;

        public Position getPosition() {
            return position;
        }

        public void setPosition(Position position) {
            this.position = position;
        }

        public static class Position {
            private String key;
            private String dataId;
            private String group = DEFAULT_GROUP;

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

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

    public static class ZkClientx {
        private String zkServers;

        public String getZkServers() {
            return zkServers;
        }

        public void setZkServers(String zkServers) {
            this.zkServers = zkServers;
        }
    }

    public static enum MetaModel {
        FILE, ZOOKEEPER
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

    public ZkClientx getZkClientx() {
        return zkClientx;
    }

    public void setZkClientx(ZkClientx zkClientx) {
        this.zkClientx = zkClientx;
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

    public MetaModel getMetaModel() {
        return metaModel;
    }

    public void setMetaModel(MetaModel metaModel) {
        this.metaModel = metaModel;
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
}
