package com.esflink.demo.sink.oureahome;

import com.alibaba.fastjson.JSONObject;
import com.esflink.demo.pojo.CreditAPI_Region;
import com.esflink.demo.pojo.OdsRegion;
import com.esflink.starter.annotation.FlinkSink;
import com.esflink.starter.common.data.DataChangeInfo;
import com.esflink.starter.common.data.FlinkJobSink;
import ma.glasnost.orika.MapperFactory;
import org.apache.doris.flink.cfg.DorisOptions;
import org.apache.doris.flink.sink.DorisSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> 同步信息到 es </p>
 *
 * @author zhouhongyin
 * @since 2023/6/9 17:11
 */

@FlinkSink(value = "mssql", database = "CreditAPI", table = "dbo.Region")
public class DemoSink implements FlinkJobSink {

    @Autowired
    private DorisSink.Builder<String> dorisBuilder;
    @Autowired
    private MapperFactory factory;

    private DorisSink.Builder<String> buildOptions() {
        DorisOptions dorisOptions = DorisOptions.builder()
                .setFenodes("192.168.6.51:8030")
                .setTableIdentifier("testdb.ods_region_2")
                .setUsername("admin")
                .setPassword("85442791")
                .build();
        dorisBuilder.setDorisOptions(dorisOptions);
        return dorisBuilder;
    }


    @Override
    public void invoke(DataChangeInfo value, Context context) throws Exception {
//        System.out.println("invoke:" + value);
    }

    @Override
    public void insert(DataChangeInfo value, Context context) throws Exception {
        System.out.println("insert:" + value.getAfterData());
    }

    @Override
    public void update(DataChangeInfo value, Context context) throws Exception {
        System.out.println("update:" + value.getAfterData());
        String afterData = value.getAfterData();
        List<String> data = new ArrayList<>();
//        CreditAPI_Region region = JSONObject.parseObject(afterData, CreditAPI_Region.class);
//        OdsRegion odsRegion = factory.getMapperFacade().map(region, OdsRegion.class);
//        data.add(JSONObject.toJSONString(odsRegion));

        data.add(afterData);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(30000);
        DorisSink.Builder<String> builder = buildOptions();
        env.fromCollection(data).sinkTo(builder.build());
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
