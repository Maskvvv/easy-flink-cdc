package com.esflink.starter.context;

import com.esflink.starter.config.EasyFlinkOrdered;
import com.esflink.starter.config.EasyFlinkProperties;
import com.esflink.starter.constants.BaseEsConstants;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * 加载配置文件
 *
 * @author zhouhongyin
 * @since 2023/5/23 16:48
 */
@Configuration
@EnableConfigurationProperties(EasyFlinkProperties.class)
@ConditionalOnProperty(name = BaseEsConstants.ENABLE_PREFIX, havingValue = "true", matchIfMissing = true)
public class FlinkListenerPropertiesConfiguration implements ApplicationContextAware, InitializingBean, Ordered {


    @Override
    public void afterPropertiesSet() throws Exception {
        //

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }

    @Override
    public int getOrder() {
        return EasyFlinkOrdered.ORDER_LISTENER_PROPERTIES;
    }
}
