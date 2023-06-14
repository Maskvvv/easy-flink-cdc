package com.esflink.starter.holder;

import com.esflink.starter.common.data.DataChangeInfo;
import com.esflink.starter.common.data.FlinkJobSink;
import com.esflink.starter.common.utils.LogUtils;
import com.esflink.starter.meta.FlinkJobIdentity;
import com.esflink.starter.meta.LogPosition;
import com.esflink.starter.meta.MetaManager;
import com.esflink.starter.prox.FlinkSinkProxy;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Flink Job 通知类
 *
 * @author zhouhongyin
 * @since 2023/5/23 14:19
 */
public class FlinkJobBus {
    public static final String BEAN_NAME = "flinkJobBus";

    private static final Map<FlinkSinkProxy, FlinkJobIdentity> FLINK_JOB_IDENTITY_MAP = new ConcurrentHashMap<>();
    private static MetaManager metaManager;


    public static void registerFlinkJobIdentity(FlinkSinkProxy flinkSinkProxy, FlinkJobIdentity flinkJobIdentity) {
        FLINK_JOB_IDENTITY_MAP.put(flinkSinkProxy, flinkJobIdentity);
    }

    /**
     * 发送通知
     */
    public static void post(FlinkJobIdentity flinkJobIdentity, DataChangeInfo dataChangeInfo, SinkFunction.Context context) throws Exception {
        List<FlinkJobSink> sinks = FlinkSinkHolder.getSink(flinkJobIdentity.getFlinkJobName());
        if (sinks == null) return;

        for (FlinkJobSink sink : sinks) {
            if (FlinkJobSinkDatabaseFilter.filter(sink, dataChangeInfo)) {
                sink.invoke(dataChangeInfo, context);
            }
        }

        updateCursor(dataChangeInfo, flinkJobIdentity);
    }

    private static void updateCursor(DataChangeInfo dataChangeInfo, FlinkJobIdentity flinkJobIdentity) {
        LogPosition logPosition = new LogPosition();
        logPosition.setFlinkJobIdentity(flinkJobIdentity);
        logPosition.setStartupTimestampMillis(dataChangeInfo.getChangeTime());
        metaManager.updateCursor(flinkJobIdentity, logPosition);
        LogUtils.info("flink job: " + flinkJobIdentity.getFlinkJobName() + ", update on " + dataChangeInfo.getChangeTime());
    }

    public static void setMetaManager(MetaManager metaManager) {
        FlinkJobBus.metaManager = metaManager;
    }

    public static MetaManager getMetaManager() {
        return metaManager;
    }
}
