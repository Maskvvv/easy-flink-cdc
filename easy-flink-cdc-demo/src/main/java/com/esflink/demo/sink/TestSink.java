package com.esflink.demo.sink;

import com.esflink.starter.annotation.FlinkSink;
import com.esflink.starter.common.data.DataChangeInfo;
import com.esflink.starter.common.data.FlinkDataChangeSink;
import com.esflink.starter.properties.EasyFlinkProperties;

/**
 * @author zhouhongyin
 * @since 2023/5/23 14:13
 */
@FlinkSink("ourea")
public class TestSink implements FlinkDataChangeSink {
    private transient EasyFlinkProperties properties = new EasyFlinkProperties();

    @Override
    public void invoke(DataChangeInfo value, Context context) throws Exception {
        System.out.println("TestSink" + value.getAfterData());
    }
}
