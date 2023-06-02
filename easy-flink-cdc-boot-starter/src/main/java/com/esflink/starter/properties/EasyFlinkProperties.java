package com.esflink.starter.properties;


import com.esflink.starter.constants.BaseEsConstants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * flink 数据库配置信息
 *
 * @author zhouhongyin
 * @since 2023/3/5 22:28
 */
@EnableConfigurationProperties(EasyFlinkProperties.class)
@ConfigurationProperties(value = BaseEsConstants.PREFIX)
@ConditionalOnProperty(name = BaseEsConstants.ENABLE_PREFIX, havingValue = "true", matchIfMissing = true)
public class EasyFlinkProperties {

    private String enable;

    private Nacos cloudConfig;

    private MetaModel metaModel;

    private Meta meta;

    private ZkClientx zkClientx;

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
        private String dataDir;

        public String getDataDir() {
            return dataDir;
        }

        public void setDataDir(String dataDir) {
            this.dataDir = dataDir;
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
}
