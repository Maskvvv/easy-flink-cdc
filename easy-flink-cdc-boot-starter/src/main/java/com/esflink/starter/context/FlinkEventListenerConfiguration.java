package com.esflink.starter.context;

import com.esflink.starter.annotation.FlinkSink;
import com.esflink.starter.config.EasyFlinkOrdered;
import com.esflink.starter.config.FlinkListenerProperties;
import com.esflink.starter.constants.BaseEsConstants;
import com.esflink.starter.data.DataChangeInfo;
import com.esflink.starter.data.FlinkDataChangeSink;
import com.esflink.starter.data.MysqlDeserialization;
import com.esflink.starter.prox.FlinkSinkProxy;
import com.esflink.starter.utils.LogUtils;
import com.ververica.cdc.connectors.mysql.MySqlSource;
import com.ververica.cdc.connectors.mysql.table.StartupOptions;
import com.ververica.cdc.debezium.DebeziumSourceFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

/**
 * Listener 配置类
 *
 * @author zhouhongyin
 * @since 2023/5/23 15:33
 */
@Configuration
@ConditionalOnProperty(name = BaseEsConstants.ENABLE_PREFIX, havingValue = "true", matchIfMissing = true)
public class FlinkEventListenerConfiguration implements ApplicationContextAware, BeanPostProcessor, InitializingBean, Ordered {

    private ApplicationContext applicationContext;

    //@Autowired
    //private ZkClientx zkClientx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<FlinkListenerProperties> flinkListenerProperties = FlinkListenerPropertiesHolder.getProperties();

        initSink();

        // 创建 flink listener
        for (FlinkListenerProperties flinkProperty : flinkListenerProperties) {
            try {
                initFlinkListener(flinkProperty);
            } catch (Exception e) {
                e.printStackTrace();
                throw new BeanCreationException("initFlinkListener失败");
            }
        }

    }

    private void initSink() {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(FlinkSink.class);
        beansWithAnnotation.forEach((key, value) -> {
            if (value instanceof FlinkDataChangeSink) {
                try {
                    FlinkSink flinkSink = value.getClass().getAnnotation(FlinkSink.class);
                    FlinkDataChangeSink sinkProxyInstance = (FlinkDataChangeSink) Proxy.newProxyInstance(value.getClass().getClassLoader(), value.getClass().getInterfaces(), new FlinkSinkProxy(value));
                    FlinkSinkHolder.registerSink(sinkProxyInstance, flinkSink);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void initFlinkListener(FlinkListenerProperties flinkProperty) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DebeziumSourceFunction<DataChangeInfo> dataChangeInfoMySqlSource = buildDataChangeSource(flinkProperty);
        DataStream<DataChangeInfo> streamSource = env
                .addSource(dataChangeInfoMySqlSource)
                .setParallelism(1);

        List<FlinkDataChangeSink> dataChangeSinks = FlinkSinkHolder.getSink(flinkProperty.getName());
        for (FlinkDataChangeSink dataChangeSink : dataChangeSinks) {
            streamSource.addSink(dataChangeSink);
        }
        env.executeAsync();
        LogUtils.formatInfo("FlinkListener %s 启动成功！", flinkProperty.getName());
    }

    /**
     * 构造变更数据源
     */
    private DebeziumSourceFunction<DataChangeInfo> buildDataChangeSource(FlinkListenerProperties flinkListenerProperties) {
        return MySqlSource.<DataChangeInfo>builder()
                .hostname(flinkListenerProperties.getHostname())
                .port(Integer.parseInt(flinkListenerProperties.getPort()))
                .databaseList(flinkListenerProperties.getDatabaseList())
                .tableList(flinkListenerProperties.getTableList())
                .username(flinkListenerProperties.getUsername())
                .password(flinkListenerProperties.getPassword())

                /**initial初始化快照,即全量导入后增量导入(检测更新数据写入)
                 * latest:只进行增量导入(不读取历史变化)
                 * timestamp:指定时间戳进行数据导入(大于等于指定时间错读取数据)
                 * 1956982
                 * 1957688
                 * 1957205
                 *
                 * 1958017
                 */
                //.startupOptions(StartupOptions.specificOffset("binlog.000005", 1957205))
                //.startupOptions(StartupOptions.timestamp(1684762706000L))
                .startupOptions(StartupOptions.latest())
                .deserializer(new MysqlDeserialization())
                .serverTimeZone("GMT+8")
                .build();
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Annotation annotation = bean.getClass().getAnnotation(FlinkSink.class);
        if (annotation != null) {
            return Proxy.newProxyInstance(bean.getClass().getClassLoader(),
                    bean.getClass().getInterfaces(),
                    new FlinkSinkProxy(bean));
        }
        return null;
    }

    @Override
    public int getOrder() {
        return EasyFlinkOrdered.ORDER_LISTENER;
    }


}
