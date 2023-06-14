package com.esflink.demo.sink.quc;

import com.esflink.starter.annotation.FlinkSink;
import com.esflink.starter.common.data.DataChangeInfo;
import com.esflink.starter.common.data.FlinkJobSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p> </p>
 *
 * @author zhouhongyin
 * @since 2023/6/14 10:02
 */
@FlinkSink(value = "ourea", database = {"QUC", "ourea"}, table = {"QUC.*", "ourea.company"})
public class QucSink1 implements FlinkJobSink {
    Logger logger = LoggerFactory.getLogger("QucSink1");

    @Override
    public void invoke(DataChangeInfo value, Context context) throws Exception {
        //logger.info("QucSink1-invoke: {}", value);

    }

    @Override
    public void insert(DataChangeInfo value, Context context) throws Exception {
        logger.info("QucSink1-insert: {}", value);
    }

    @Override
    public void update(DataChangeInfo value, Context context) throws Exception {
        logger.info("QucSink1-update: {}", value);
    }

    @Override
    public void delete(DataChangeInfo value, Context context) throws Exception {
        logger.info("QucSink1-delete: {}", value);
        throw new RuntimeException("啊，发生错误了！");
    }

    @Override
    public void handleError(DataChangeInfo value, Context context, Throwable throwable) {

        logger.error("QucSink1-errorHandle: {}", value);
        logger.error("QucSink1-errorHandle: {}", throwable.getMessage());
    }
}
