package com.esflink.starter.configuration;

import com.esflink.starter.annotation.FlinkSink;
import com.esflink.starter.common.data.DataChangeInfo;
import com.esflink.starter.common.data.FlinkJobSink;
import com.esflink.starter.common.data.MssqlDeserialization;
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
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.cdc.connectors.mysql.source.MySqlSource;
import org.apache.flink.cdc.connectors.mysql.table.StartupOptions;
import org.apache.flink.cdc.connectors.sqlserver.source.SqlServerSourceBuilder;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
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
@ConditionalOnProperty(name = BaseEsConstants.ENABLE_PREFIX, havingValue = "true", matchIfMissing = false)
public class FlinkJobConfiguration implements ApplicationContextAware, SmartInitializingSingleton, Ordered {
    Logger logger = LoggerFactory.getLogger(FlinkJobConfiguration.class.getName());

    private ApplicationContext applicationContext;

    @Autowired
    private EasyFlinkProperties easyFlinkProperties;

    @Override
    public void afterSingletonsInstantiated() {

        List<FlinkJobProperties> flinkJobProperties = FlinkJobPropertiesHolder.getProperties();

        initSink();

        // 创建 flink job
        for (FlinkJobProperties flinkProperty : flinkJobProperties) {
            try {
                initFlinkJob(flinkProperty);
            } catch (Exception e) {
                logger.error("init Flink job [" + flinkProperty.getName() + "] failed!", e);
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
                    logger.error("init Flink sink [" + key + "] failed!", e);
                }

            }
        });
    }

    private void initFlinkJob(FlinkJobProperties flinkProperty) throws Exception {
        List<FlinkJobSink> dataChangeSinks = FlinkSinkHolder.getSink(flinkProperty.getName());
        if (CollectionUtils.isEmpty(dataChangeSinks)) {
            logger.warn("There are no sink under the Flink Job [{}]!", flinkProperty.getName());
            return;
        }
        FlinkJobIdentity flinkJobIdentity = FlinkJobIdentity.generate(easyFlinkProperties.getMeta(), flinkProperty.getName());

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStream<DataChangeInfo> streamSource = null;
        if ("MYSQL".equalsIgnoreCase(flinkProperty.getDbType())) {
            MySqlSource<DataChangeInfo> dataChangeInfoMySqlSource = buildMysqlDataChangeSource(flinkProperty, flinkJobIdentity);
            streamSource = env
                    .fromSource(dataChangeInfoMySqlSource, WatermarkStrategy.noWatermarks(), "MYSQLIncrementalSource")
                    .setParallelism(1);
        }
        if ("MSSQL".equalsIgnoreCase(flinkProperty.getDbType())) {
            SqlServerSourceBuilder.SqlServerIncrementalSource<DataChangeInfo> dataChangeInfoMySqlSource = buildMssqlDataChangeSource(flinkProperty, flinkJobIdentity);
            streamSource = env
                    .fromSource(dataChangeInfoMySqlSource, WatermarkStrategy.noWatermarks(), "MSSQLIncrementalSource")
                    .setParallelism(1);
        }
        FlinkJobSink sinkProxyInstance = (FlinkJobSink) Proxy.newProxyInstance(
                FlinkJobSink.class.getClassLoader(),
                new Class<?>[]{FlinkJobSink.class},
                new FlinkSinkProxy(flinkJobIdentity));
        streamSource.addSink(sinkProxyInstance);

        env.executeAsync();

        logger.info("Flink Job [{}] success", flinkProperty.getName());
    }

    /**
     * 构造MYSQL变更数据源
     */
    private MySqlSource<DataChangeInfo> buildMysqlDataChangeSource(FlinkJobProperties flinkJobProperties, FlinkJobIdentity flinkJobIdentity) {
        MetaManager metaManager = FlinkJobBus.getMetaManager();
        LogPosition cursor = metaManager.getCursor(flinkJobIdentity);

        StartupOptions startupOptions = null;
        // 有 cursor 信息，默认 TIMESTAMP 方式启动
        if (cursor != null) {
            startupOptions = StartupOptions.timestamp(cursor.getStartupTimestampMillis() + 1);
        }

        return MySqlSource.<DataChangeInfo>builder()
                .hostname(flinkJobProperties.getHostname())
                .port(Integer.parseInt(flinkJobProperties.getPort()))
                .databaseList(flinkJobProperties.getDatabaseList().split(","))
                .tableList(flinkJobProperties.getTableList())
                .username(flinkJobProperties.getUsername())
                .password(flinkJobProperties.getPassword())
                /*initial初始化快照,即全量导入后增量导入(检测更新数据写入)
                 * latest:只进行增量导入(不读取历史变化)
                 * timestamp:指定时间戳进行数据导入(大于等于指定时间错读取数据)
                 */
                .startupOptions(startupOptions != null ? startupOptions : flinkJobProperties.getMysqlStartupOptions())
                .deserializer(new MysqlDeserialization())
                .serverTimeZone(flinkJobProperties.getServerTimeZone())
                .build();
    }

    /**
     * 构造MSSQL变更数据源
     */
    private SqlServerSourceBuilder.SqlServerIncrementalSource<DataChangeInfo> buildMssqlDataChangeSource(FlinkJobProperties flinkJobProperties, FlinkJobIdentity flinkJobIdentity) {
        /*MetaManager metaManager = FlinkJobBus.getMetaManager();
        LogPosition cursor = metaManager.getCursor(flinkJobIdentity);
        org.apache.flink.cdc.connectors.base.options.StartupOptions startupOptions = null;
        // 有 cursor 信息，默认 TIMESTAMP 方式启动
        if (cursor != null) {
            startupOptions = org.apache.flink.cdc.connectors.base.options.StartupOptions.timestamp(cursor.getStartupTimestampMillis() + 1);
        }*/

        return SqlServerSourceBuilder.SqlServerIncrementalSource.<DataChangeInfo>builder()
                .hostname(flinkJobProperties.getHostname())
                .port(Integer.parseInt(flinkJobProperties.getPort()))
                .databaseList(flinkJobProperties.getDatabaseList().split(","))
                .tableList(flinkJobProperties.getTableList())
                .username(flinkJobProperties.getUsername())
                .password(flinkJobProperties.getPassword())
                .deserializer(new MssqlDeserialization())
//                .startupOptions(startupOptions != null ? startupOptions : flinkJobProperties.getMssqlStartupOptions())
                .startupOptions(flinkJobProperties.getMssqlStartupOptions()) //MSSQL仅支持 初始化和latest 方式
                .serverTimeZone(flinkJobProperties.getServerTimeZone())
                .build();
    }


    @Override
    public int getOrder() {
        return EasyFlinkOrdered.FLINK_JOB;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
