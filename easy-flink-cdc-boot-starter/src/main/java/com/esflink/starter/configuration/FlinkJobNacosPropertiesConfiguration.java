package com.esflink.starter.configuration;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.esflink.starter.constants.BaseEsConstants;
import com.esflink.starter.properties.EasyFlinkOrdered;
import com.esflink.starter.properties.EasyFlinkProperties;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.Executor;

@Configuration
@EnableConfigurationProperties(EasyFlinkProperties.class)
@ConditionalOnClass(NacosConfigManager.class)
@ConditionalOnProperty(name = {BaseEsConstants.ENABLE_PREFIX, BaseEsConstants.ENABLE_NACOS}, havingValue = "true", matchIfMissing = false)
public class FlinkJobNacosPropertiesConfiguration implements BeanFactoryPostProcessor, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(FlinkJobNacosPropertiesConfiguration.class);

    private final NacosConfigManager nacosConfigManager;

    @Value("${spring.cloud.nacos.config.extension-configs[1].group}")
    private String group;

    @Value("${spring.cloud.nacos.config.extension-configs[1].data-id}")
    private String dataId;

    public FlinkJobNacosPropertiesConfiguration(NacosConfigManager nacosConfigManager) {
        this.nacosConfigManager = nacosConfigManager;
    }

    @PostConstruct
    public void getConfig() throws NacosException {
        //NacosConfigManager nacosConfigManager = this.nacosConfigManager;
        //ConfigService configService = nacosConfigManager.getConfigService();
        //
        //String config = configService.getConfig(dataId, group, 2000);
        //
        //System.out.println(config);
    }

    @PostConstruct
    public void addListener() throws NacosException {

        NacosConfigManager nacosConfigManager = this.nacosConfigManager;
        ConfigService configService = nacosConfigManager.getConfigService();

        logger.info("-------------------------addListener---------------------------");

        configService.addListener(dataId, group, new Listener() {
            @Override
            public Executor getExecutor() {
                return null;
            }

            @Override
            public void receiveConfigInfo(String configInfo) {
                System.out.println("--------------------------------------");

                Config load = ConfigFactory.parseString(configInfo);

                //load.entrySet().forEach(entry -> {
                //    System.out.println(entry.getKey() + "\t" + entry.getValue());
                //});

                String mapping = load.getString("mapping");
                System.out.println(mapping);

                List<String> like = load.getStringList("like");
                for (String s : like) {
                    System.out.println(s);
                }


            }
        });
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public int getOrder() {
        return EasyFlinkOrdered.NACOS_PROPERTIES_CONFIG;
    }
}


