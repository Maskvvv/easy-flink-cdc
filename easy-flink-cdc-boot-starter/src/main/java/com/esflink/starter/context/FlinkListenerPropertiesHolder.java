package com.esflink.starter.context;

import com.esflink.starter.annotation.FlinkSink;
import com.esflink.starter.config.FlinkListenerProperties;
import com.esflink.starter.data.FlinkDataChangeSink;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * FlinkListenerProperties 工厂
 *
 * @author zhouhongyin
 * @since 2023/5/23 14:19
 */
public class FlinkListenerPropertiesHolder {
    public static final String BEAN_NAME = "flinkListenerHolder";

    private static final List<FlinkListenerProperties> PROPERTIES = new CopyOnWriteArrayList<>();

    public static void registerProperties(FlinkDataChangeSink sink, FlinkSink flinkSink) {

    }

    public static List<FlinkListenerProperties> getProperties() {
        return PROPERTIES;
    }

}
