package com.esflink.starter.prox;

import com.esflink.starter.common.data.DataChangeInfo;
import com.esflink.starter.holder.FlinkJobBus;
import com.esflink.starter.meta.FlinkJobIdentity;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 代理类
 *
 * @author zhouhongyin
 * @since 2023/5/28 15:12
 */
public class FlinkSinkProxy implements InvocationHandler, Serializable {

    private final FlinkJobIdentity flinkJobIdentity;
    Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public FlinkSinkProxy(FlinkJobIdentity flinkJobIdentity) {
        this.flinkJobIdentity = flinkJobIdentity;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        DataChangeInfo dataChangeInfo = null;
        try {
            if (!"invoke".equals(method.getName())) return null;
            dataChangeInfo = (DataChangeInfo) args[0];
            SinkFunction.Context context = (SinkFunction.Context) args[1];
            FlinkJobBus.post(flinkJobIdentity, dataChangeInfo, context);

            logger.info("flinkJobIdentity: {}, dataChangeInfo: {}", flinkJobIdentity, dataChangeInfo);
        } catch (Exception e) {
            logger.error("An error occurred while post the flinkJob[flinkJobIdentity: " + flinkJobIdentity + "]!", e);
        }
        return null;
    }

}
