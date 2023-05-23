package com.esflink.demo.sink;

import com.esflink.starter.annotation.FlinkSink;
import com.esflink.starter.data.DataChangeInfo;
import com.esflink.starter.data.FlinkDataChangeSink;

/**
 * @author zhouhongyin
 * @since 2023/5/23 14:13
 */
@FlinkSink("ourea")
public class Test2Sink implements FlinkDataChangeSink {
    @Override
    public void invoke(DataChangeInfo value, Context context) throws Exception {
        System.out.println("TestSink2" + value.getAfterData());
    }
}
