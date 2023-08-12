package com.esflink.starter.configuration;

import com.esflink.starter.constants.BaseEsConstants;
import com.esflink.starter.meta.ZookeeperMixedMetaManager;
import com.esflink.starter.properties.EasyFlinkOrdered;
import com.esflink.starter.properties.EasyFlinkProperties;
import com.esflink.starter.zookeeper.ZkClientx;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * Zookeeper 配置类
 *
 * @author zhouhongyin
 * @since 2023/5/23 15:33
 */
@EnableConfigurationProperties(EasyFlinkProperties.class)
@Configuration
@ConditionalOnProperty(name = BaseEsConstants.ENABLE_PREFIX, havingValue = "true")
public class FlinkZookeeperConfiguration implements Ordered {
    public static final String ZOOKEEPER_ADDRESS = "127.0.0.1:21810";
    @Autowired
    private EasyFlinkProperties easyFlinkProperties;

    @Bean
    @ConditionalOnProperty(name = BaseEsConstants.META_MODEL, havingValue = ZookeeperMixedMetaManager.NAME)
    public ZkClientx zkClientx() {
        String address = StringUtils.defaultString(easyFlinkProperties.getZookeeper().getAddress(), ZOOKEEPER_ADDRESS);
        ZkClientx zkClient = new ZkClientx(address);
        return zkClient;
    }

    @Override
    public int getOrder() {
        return EasyFlinkOrdered.ORDER_ZOOKEEPER;
    }
}
