package com.esflink.starter.context;

import com.esflink.starter.config.FlinkListenerProperties;

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

    public static void registerProperties(FlinkListenerProperties properties) {
        PROPERTIES.add(properties);
    }

    public static void registerAllProperties(List<FlinkListenerProperties> properties) {
        PROPERTIES.addAll(properties);
    }

    public static List<FlinkListenerProperties> getProperties() {
        return PROPERTIES;
    }

}
