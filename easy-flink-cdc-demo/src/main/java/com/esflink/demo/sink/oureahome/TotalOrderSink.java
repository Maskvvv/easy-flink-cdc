package com.esflink.demo.sink.oureahome;

import com.alibaba.fastjson.JSONObject;
import com.esflink.demo.pojo.CreditAPI_Region;
import com.esflink.demo.pojo.CreditAPI_TotalOrder;
import com.esflink.demo.pojo.OdsRegion;
import com.esflink.demo.pojo.OdsTotalOrder;
import com.esflink.starter.annotation.FlinkSink;
import com.esflink.starter.common.data.DataChangeInfo;
import com.esflink.starter.common.data.FlinkJobSink;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFactory;
import org.apache.doris.flink.cfg.DorisExecutionOptions;
import org.apache.doris.flink.cfg.DorisOptions;
import org.apache.doris.flink.cfg.DorisReadOptions;
import org.apache.doris.flink.sink.DorisSink;
import org.apache.doris.flink.sink.writer.serializer.SimpleStringSerializer;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * <p> 同步信息到 es </p>
 *
 * @author zhouhongyin
 * @since 2023/6/9 17:11
 */

@Slf4j
@FlinkSink(value = "mssql", database = "CreditAPI", table = "dbo.TotalOrder")
public class TotalOrderSink implements FlinkJobSink {

    @Autowired
    private DorisSink.Builder<String> dorisBuilder;
    @Autowired
    private MapperFactory factory;

    private void buildOptions() {
        DorisOptions dorisOptions = DorisOptions.builder()
                .setFenodes("192.168.6.51:8030")
                .setTableIdentifier("testdb.ods_totalorder")
                .setUsername("admin")
                .setPassword("85442791")
                .build();
        dorisBuilder.setDorisOptions(dorisOptions);
    }


    @Override
    public void invoke(DataChangeInfo value, Context context) throws Exception {
        System.out.println("invoke:" + value);
    }

    /**
     * 存在问题， 变更记录的时间比当前时间多8小时
     *
     * @param value
     * @param context
     * @throws Exception
     */
    @Override
    public void insert(DataChangeInfo value, Context context) throws Exception {
        System.out.println("insert:" + value);
        String afterData = value.getAfterData();
        List<String> data = new ArrayList<>();
//        CreditAPI_TotalOrder source = JSONObject.parseObject(afterData, CreditAPI_TotalOrder.class);
//        log.info("{}", source);
//        OdsTotalOrder totalOrder = factory.getMapperFacade().map(source, OdsTotalOrder.class);
//        data.add(JSONObject.toJSONString(totalOrder));

        data.add(afterData);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(30000);
        buildOptions();
        env.fromCollection(data).sinkTo(dorisBuilder.build());
        env.execute("doris test");
    }

    @Override
    public void update(DataChangeInfo value, Context context) throws Exception {
        System.out.println("update:" + value);
        String afterData = value.getAfterData();
        List<String> data = new ArrayList<>();
//        CreditAPI_TotalOrder source = JSONObject.parseObject(afterData, CreditAPI_TotalOrder.class);
//        OdsTotalOrder totalOrder = factory.getMapperFacade().map(source, OdsTotalOrder.class);
//        data.add(JSONObject.toJSONString(totalOrder));

        data.add(afterData);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(30000);
        buildOptions();
        env.fromCollection(data).sinkTo(dorisBuilder.build());
        env.execute("doris test");
    }

    @Override
    public void delete(DataChangeInfo value, Context context) throws Exception {
        System.out.println("delete:" + value);
    }

    @Override
    public void handleError(DataChangeInfo value, Context context, Throwable throwable) {

    }
}
