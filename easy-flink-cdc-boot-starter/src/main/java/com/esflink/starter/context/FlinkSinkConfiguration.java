package com.esflink.starter.context;

import com.esflink.starter.annotation.FlinkSink;
import com.esflink.starter.config.EasyFlinkOrdered;
import com.esflink.starter.constants.BaseEsConstants;
import com.esflink.starter.data.FlinkDataChangeSink;
import com.esflink.starter.prox.FlinkSinkProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * sink 注册类
 *
 * @author zhouhongyin
 * @since 2023/5/23 14:19
 */
@Configuration
@ConditionalOnProperty(name = BaseEsConstants.ENABLE_PREFIX, havingValue = "true", matchIfMissing = true)
public class FlinkSinkConfiguration implements ApplicationContextAware, BeanFactoryPostProcessor, Ordered {
    private ApplicationContext applicationContext;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;
        beanFactory.addBeanPostProcessor(applicationContext.getBean(FlinkEventListenerConfiguration.class));

        Map<String, Object> beansWithAnnotation = defaultListableBeanFactory.getBeansWithAnnotation(FlinkSink.class);

        beansWithAnnotation.forEach((key, value) -> {
            if (value instanceof FlinkDataChangeSink) {

                try {


                    Field field = value.getClass().getDeclaredField("sink");
                    Object target = field.get(value);
                    FlinkSink flinkSink = target.getClass().getAnnotation(FlinkSink.class);
                    FlinkSinkHolder.registerSink((FlinkDataChangeSink) value, flinkSink);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
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
