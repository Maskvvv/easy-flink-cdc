package com.esflink.starter.common.data;


import org.apache.flink.streaming.api.functions.sink.SinkFunction;

/**
 * 消息接收的处理类
 *
 * @author zhouhongyin
 * @since 2023/3/5 23:04
 */
public interface FlinkJobSink extends SinkFunction<DataChangeInfo> {

    @Override
    default void invoke(DataChangeInfo value, Context context) throws Exception {

    }

    default void insert(DataChangeInfo value, Context context) throws Exception {

    }

    default void update(DataChangeInfo value, Context context) throws Exception {

    }

    default void delete(DataChangeInfo value, Context context) throws Exception {

    }

    default void handleError(DataChangeInfo value, Context context, Throwable throwable) {

    }


}
