package com.esflink.starter.configuration;

import com.esflink.starter.constants.BaseEsConstants;
import com.esflink.starter.properties.EasyFlinkOrdered;
import com.esflink.starter.properties.EasyFlinkProperties;
import com.esflink.starter.zookeeper.ZkClientx;
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
@ConditionalOnProperty(name = BaseEsConstants.ENABLE_PREFIX, havingValue = "true", matchIfMissing = false)
public class FlinkZookeeperConfiguration implements Ordered {

    @Autowired
    private EasyFlinkProperties easyFlinkProperties;

    @Bean
    @ConditionalOnProperty(name = BaseEsConstants.META_MODEL, havingValue = "zookeeper")
    public ZkClientx zkClientx() {
        ZkClientx zkClient = new ZkClientx(easyFlinkProperties.getZkClientx().getZkServers());
        return zkClient;
    }

    @Override
    public int getOrder() {
        return EasyFlinkOrdered.ORDER_ZOOKEEPER;
    }
}
