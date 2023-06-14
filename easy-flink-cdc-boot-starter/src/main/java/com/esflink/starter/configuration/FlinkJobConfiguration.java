package com.esflink.starter.configuration;

import com.esflink.starter.annotation.FlinkSink;
import com.esflink.starter.common.data.DataChangeInfo;
import com.esflink.starter.common.data.FlinkJobSink;
import com.esflink.starter.common.data.MysqlDeserialization;
import com.esflink.starter.constants.BaseEsConstants;
import com.esflink.starter.holder.FlinkJobBus;
import com.esflink.starter.holder.FlinkJobPropertiesHolder;
import com.esflink.starter.holder.FlinkSinkHolder;
import com.esflink.starter.meta.FlinkJobIdentity;
import com.esflink.starter.meta.LogPosition;
import com.esflink.starter.meta.MetaManager;
import com.esflink.starter.properties.EasyFlinkOrdered;
import com.esflink.starter.properties.EasyFlinkProperties;
import com.esflink.starter.properties.FlinkJobProperties;
import com.esflink.starter.prox.FlinkSinkProxy;
import com.ververica.cdc.connectors.mysql.MySqlSource;
import com.ververica.cdc.connectors.mysql.table.StartupOptions;
import com.ververica.cdc.debezium.DebeziumSourceFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;

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
public class FlinkJobConfiguration implements ApplicationContextAware, SmartInitializingSingleton, EnvironmentAware, Ordered {
    Logger logger = LoggerFactory.getLogger(FlinkJobConfiguration.class.getName());

    private ApplicationContext applicationContext;

    private Environment environment;

    @Autowired
    private EasyFlinkProperties easyFlinkProperties;

    //@Autowired
    //private ZkClientx zkClientx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    @Override
    public void afterSingletonsInstantiated() {

        List<FlinkJobProperties> flinkJobProperties = FlinkJobPropertiesHolder.getProperties();

        initSink();

        // 创建 flink job
        for (FlinkJobProperties flinkProperty : flinkJobProperties) {
            try {
                initFlinkJob(flinkProperty);
            } catch (Exception e) {
                e.printStackTrace();
                throw new BeanCreationException("init Flink job failed!");
            }
        }

    }

    /**
     * 初始化 sink
     */
    private void initSink() {

        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(FlinkSink.class);
        beansWithAnnotation.forEach((key, value) -> {
            if (value instanceof FlinkJobSink) {
                try {
                    FlinkSink flinkSink = value.getClass().getAnnotation(FlinkSink.class);
                    FlinkSinkHolder.registerSink((FlinkJobSink) value, flinkSink);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void initFlinkJob(FlinkJobProperties flinkProperty) throws Exception {
        List<FlinkJobSink> dataChangeSinks = FlinkSinkHolder.getSink(flinkProperty.getName());
        if (CollectionUtils.isEmpty(dataChangeSinks)) return;
        FlinkJobIdentity flinkJobIdentity = FlinkJobIdentity.generate(easyFlinkProperties.getMeta(), flinkProperty.getName());

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DebeziumSourceFunction<DataChangeInfo> dataChangeInfoMySqlSource = buildDataChangeSource(flinkProperty, flinkJobIdentity);
        DataStream<DataChangeInfo> streamSource = env
                .addSource(dataChangeInfoMySqlSource)
                .setParallelism(1);

        FlinkJobSink sinkProxyInstance = (FlinkJobSink) Proxy.newProxyInstance(
                FlinkJobSink.class.getClassLoader(),
                new Class<?>[]{FlinkJobSink.class},
                new FlinkSinkProxy(flinkJobIdentity));
        streamSource.addSink(sinkProxyInstance);

        env.executeAsync();

        logger.info("Flink Job [{}] 启动成功！", flinkProperty.getName());
    }

    /**
     * 构造变更数据源
     */
    private DebeziumSourceFunction<DataChangeInfo> buildDataChangeSource(FlinkJobProperties flinkJobProperties, FlinkJobIdentity flinkJobIdentity) {
        MetaManager metaManager = FlinkJobBus.getMetaManager();
        LogPosition cursor = metaManager.getCursor(flinkJobIdentity);
        StartupOptions startupOptions = null;
        if (cursor != null) {
            startupOptions = StartupOptions.timestamp(cursor.getStartupTimestampMillis());
        }

        return MySqlSource.<DataChangeInfo>builder()
                .hostname(flinkJobProperties.getHostname())
                .port(Integer.parseInt(flinkJobProperties.getPort()))
                .databaseList(flinkJobProperties.getDatabaseList())
                .tableList(flinkJobProperties.getTableList())
                .username(flinkJobProperties.getUsername())
                .password(flinkJobProperties.getPassword())

                /*initial初始化快照,即全量导入后增量导入(检测更新数据写入)
                 * latest:只进行增量导入(不读取历史变化)
                 * timestamp:指定时间戳进行数据导入(大于等于指定时间错读取数据)
                 */
                .startupOptions(startupOptions != null ? startupOptions : flinkJobProperties.getStartupOptions())
                .deserializer(new MysqlDeserialization())
                .serverTimeZone("GMT+8")
                .build();
    }


    @Override
    public int getOrder() {
        return EasyFlinkOrdered.ORDER_LISTENER;
    }


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
