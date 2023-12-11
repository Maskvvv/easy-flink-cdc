package com.esflink.demo.sink.oureahome;

import com.esflink.starter.common.data.DataChangeInfo;
import com.esflink.starter.common.data.FlinkJobSink;

/**
 * <p> 同步信息到 es </p>
 *
 * @author zhouhongyin
 * @since 2023/6/9 17:11
 */

//@FlinkSink(value = "ourea", database = "ourea", table = "ourea.company")
public class OureaSink implements FlinkJobSink {


    @Override
    public void invoke(DataChangeInfo value, Context context) throws Exception {

    }

    @Override
    public void insert(DataChangeInfo value, Context context) throws Exception {

    }

    @Override
    public void update(DataChangeInfo value, Context context) throws Exception {
        System.out.println(value);
    }

    @Override
    public void delete(DataChangeInfo value, Context context) throws Exception {

    }

    @Override
    public void handleError(DataChangeInfo value, Context context, Throwable throwable) {

    }
}
