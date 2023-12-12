package com.esflink.starter.configuration;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigService;
import com.esflink.starter.constants.BaseEsConstants;
import com.esflink.starter.holder.FlinkJobPropertiesHolder;
import com.esflink.starter.properties.EasyFlinkOrdered;
import com.esflink.starter.properties.EasyFlinkProperties;
import com.esflink.starter.properties.FlinkJobProperties;
import com.esflink.starter.properties.parser.DefaultFlinkListenerPropertiesParser;
import com.esflink.starter.properties.parser.FlinkListenerPropertiesParser;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.util.List;

@Configuration
@EnableConfigurationProperties(EasyFlinkProperties.class)
@ConditionalOnClass(NacosConfigManager.class)
@ConditionalOnProperty(name = {BaseEsConstants.ENABLE_NACOS}, havingValue = "true", matchIfMissing = false)
public class FlinkJobNacosPropertiesConfiguration implements BeanFactoryPostProcessor, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(FlinkJobNacosPropertiesConfiguration.class);


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        try {

            NacosConfigManager nacosConfigManager = beanFactory.getBean(NacosConfigManager.class);
            EasyFlinkProperties easyFlinkProperties = beanFactory.getBean(EasyFlinkProperties.class);

            if (nacosConfigManager == null || nacosConfigManager.getNacosConfigProperties() == null
                    || easyFlinkProperties == null) {
                logger.warn("no properties of nacos config found, can't load config from nacos!");
                return;
            }

            EasyFlinkProperties.Nacos cloudConfig = easyFlinkProperties.getCloudConfig();

            // 获取 nacos 服务连接实例，用于请求配置文件
            ConfigService configService = nacosConfigManager.getConfigService();
            if (configService == null) {
                logger.warn("no instance of nacos config service found, can't load config from nacos!");
                return;
            }

            EasyFlinkProperties.Nacos.Position position = cloudConfig.getPosition();
            String configString = configService.getConfig(position.getDataId(), position.getGroup(), nacosConfigManager.getNacosConfigProperties().getTimeout());
            if (StringUtils.isBlank(configString)) {
                logger.warn("no config of nacos found, cannot load config from nacos!");
                return;
            }

            Config config = ConfigFactory.parseString(configString);
            FlinkListenerPropertiesParser flinkPropertiesParser = new DefaultFlinkListenerPropertiesParser();
            List<FlinkJobProperties> properties = flinkPropertiesParser.parse(config);
            FlinkJobPropertiesHolder.registerAllProperties(properties);

        } catch (Exception e) {
            logger.error("load config from nacos fail!", e);
        }

    }

    @Override
    public int getOrder() {
        return EasyFlinkOrdered.NACOS_PROPERTIES_CONFIG;
    }
}


