package com.esflink.starter.holder;

import com.esflink.starter.properties.FlinkJobProperties;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * flinkJobPropertiesHolder 工厂
 *
 * @author zhouhongyin
 * @since 2023/5/23 14:19
 */
public class FlinkJobPropertiesHolder {
    public static final String BEAN_NAME = "flinkJobPropertiesHolder";

    private static final List<FlinkJobProperties> PROPERTIES = new CopyOnWriteArrayList<>();

    public static void registerProperties(FlinkJobProperties properties) {
        PROPERTIES.add(properties);
    }

    public static void registerAllProperties(List<FlinkJobProperties> properties) {
        PROPERTIES.addAll(properties);
    }

    public static List<FlinkJobProperties> getProperties() {
        return PROPERTIES;
    }

}
