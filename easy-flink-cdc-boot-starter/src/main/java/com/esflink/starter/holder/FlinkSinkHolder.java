package com.esflink.starter.holder;

import com.esflink.starter.annotation.FlinkSink;
import com.esflink.starter.common.data.FlinkJobSink;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * FlinkSink 工厂
 *
 * @author zhouhongyin
 * @since 2023/5/23 14:19
 */
public class FlinkSinkHolder {
    public static final String BEAN_NAME = "flinkSinkHolder";

    private static final Map<String, List<FlinkJobSink>> SINK_MAP = new ConcurrentHashMap<>();

    public static void registerSink(FlinkJobSink sink, FlinkSink flinkSink) {
        String value = flinkSink.value();
        SINK_MAP.computeIfAbsent(value, key -> new CopyOnWriteArrayList<>()).add(sink);
    }

    public static List<FlinkJobSink> getSink(String flinkJobName) {
        return SINK_MAP.get(flinkJobName);
    }

}
