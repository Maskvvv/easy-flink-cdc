package com.esflink.starter.context;

import com.esflink.starter.config.EasyFlinkOrdered;
import com.esflink.starter.config.EasyFlinkProperties;
import com.esflink.starter.config.FlinkListenerProperties;
import com.esflink.starter.config.parser.DefaultFlinkListenerPropertiesParser;
import com.esflink.starter.config.parser.FlinkListenerPropertiesParser;
import com.esflink.starter.constants.BaseEsConstants;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.List;

/**
 * 加载配置文件
 *
 * @author zhouhongyin
 * @since 2023/5/23 16:48
 */
@Configuration
@EnableConfigurationProperties(EasyFlinkProperties.class)
@ConditionalOnProperty(name = BaseEsConstants.ENABLE_PREFIX, havingValue = "true", matchIfMissing = true)
public class FlinkListenerPropertiesConfiguration implements ApplicationContextAware, BeanFactoryPostProcessor, Ordered {

    @Autowired
    private EasyFlinkProperties easyFlinkProperties;


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 获取配置文件 resource
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource(BaseEsConstants.CONFIG_FILE);
        // 解析 conf resource 为 FlinkListenerProperties
        FlinkListenerPropertiesParser flinkPropertiesParser = new DefaultFlinkListenerPropertiesParser();
        List<FlinkListenerProperties> flinkListenerProperties = flinkPropertiesParser.getProperties(resource);

        // 注册 FlinkListenerProperties 到 FlinkListenerPropertiesHolder
        FlinkListenerPropertiesHolder.registerAllProperties(flinkListenerProperties);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }

    @Override
    public int getOrder() {
        return EasyFlinkOrdered.ORDER_LISTENER_PROPERTIES;
    }
}
