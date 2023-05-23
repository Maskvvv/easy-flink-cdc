package com.esflink.starter.context;

import com.esflink.starter.annotation.FlinkSink;
import com.esflink.starter.config.EasyFlinkOrdered;
import com.esflink.starter.constants.BaseEsConstants;
import com.esflink.starter.data.FlinkDataChangeSink;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * sink 注册类
 *
 * @author zhouhongyin
 * @since 2023/5/23 14:19
 */
@Configuration
@ConditionalOnProperty(name = BaseEsConstants.ENABLE_PREFIX, havingValue = "true", matchIfMissing = true)
public class FlinkSinkConfiguration implements ApplicationContextAware, BeanPostProcessor, Ordered {
    private ApplicationContext applicationContext;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        FlinkSink flinkSink = bean.getClass().getAnnotation(FlinkSink.class);
        if (flinkSink != null) {
            FlinkSinkHolder.registerSink((FlinkDataChangeSink) bean, flinkSink);
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
