package com.esflink.starter.context;

import com.esflink.starter.annotation.FlinkSink;
import com.esflink.starter.config.EasyFlinkOrdered;
import com.esflink.starter.constants.BaseEsConstants;
import com.esflink.starter.data.FlinkDataChangeSink;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * @author zhouhongyin
 * @since 2023/5/23 14:19
 */
@Configuration
@ConditionalOnProperty(prefix = BaseEsConstants.ENABLE_PREFIX, havingValue = "true", matchIfMissing = true)
public class FlinkSinkConfiguration implements ApplicationContextAware, BeanPostProcessor, Ordered {
    private ApplicationContext applicationContext;

    @Bean(FlinkSinkHolder.BEAN_NAME)
    @ConditionalOnMissingBean
    public FlinkSinkHolder flinkSinkHolder() {
        return new FlinkSinkHolder();
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        FlinkSink flinkSink = bean.getClass().getAnnotation(FlinkSink.class);
        if (flinkSink != null) {
            FlinkSinkHolder flinkSinkHolder = applicationContext.getBean(FlinkSinkHolder.BEAN_NAME, FlinkSinkHolder.class);
            flinkSinkHolder.registerSink((FlinkDataChangeSink) bean, flinkSink);
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public int getOrder() {
        return EasyFlinkOrdered.ORDER_SINK;
    }
}
