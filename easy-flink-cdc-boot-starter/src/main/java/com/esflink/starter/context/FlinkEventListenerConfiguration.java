package com.esflink.starter.context;

import com.esflink.starter.config.EasyFlinkOrdered;
import com.esflink.starter.config.FlinkProperties;
import com.esflink.starter.constants.BaseEsConstants;
import com.esflink.starter.data.DataChangeInfo;
import com.esflink.starter.data.FlinkDataChangeSink;
import com.esflink.starter.data.MysqlDeserialization;
import com.ververica.cdc.connectors.mysql.MySqlSource;
import com.ververica.cdc.connectors.mysql.table.StartupOptions;
import com.ververica.cdc.debezium.DebeziumSourceFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.util.List;

/**
 * Listener 配置类
 *
 * @author zhouhongyin
 * @since 2023/5/23 15:33
 */
@Configuration
@EnableConfigurationProperties(FlinkProperties.class)
@ConditionalOnProperty(name = BaseEsConstants.ENABLE_PREFIX, havingValue = "true", matchIfMissing = true)
public class FlinkEventListenerConfiguration implements ApplicationContextAware, InitializingBean, Ordered {
    @Autowired
    private FlinkProperties flinkProperties;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DebeziumSourceFunction<DataChangeInfo> dataChangeInfoMySqlSource = buildDataChangeSource();
        DataStream<DataChangeInfo> streamSource = env
                .addSource(dataChangeInfoMySqlSource, "mysql-source")
                .setParallelism(1);
        List<FlinkDataChangeSink> dataChangeSinks = FlinkSinkHolder.getSink("test");
        for (FlinkDataChangeSink dataChangeSink : dataChangeSinks) {
            streamSource.addSink(dataChangeSink);
        }
        env.executeAsync("mysql-stream-cdc");
    }

    /**
     * 构造变更数据源
     */
    private DebeziumSourceFunction<DataChangeInfo> buildDataChangeSource() {
        return MySqlSource.<DataChangeInfo>builder()
                .hostname(flinkProperties.getHostname())
                .port(flinkProperties.getPort())
                .databaseList(flinkProperties.getDatabaseList())
                .tableList(flinkProperties.getTableList())
                .username(flinkProperties.getUsername())
                .password(flinkProperties.getPassword())

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
                .startupOptions(StartupOptions.timestamp(1684762706000L))
                .deserializer(new MysqlDeserialization())
                .serverTimeZone("GMT+8")
                .build();
    }

    @Override
    public int getOrder() {
        return EasyFlinkOrdered.ORDER_LISTENER;
    }


}
