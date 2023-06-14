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
        logger.info("QucSink1: {}", value);

    }

    public static void main(String[] args) {
        System.out.println(QucSink1.class.getName());
    }
}
