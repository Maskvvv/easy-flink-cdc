package com.esflink.starter.holder;

import com.esflink.starter.properties.FlinkJobProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * flinkJobPropertiesHolder
 *
 * @author zhouhongyin
 * @since 2023/5/23 14:19
 */
public class FlinkJobPropertiesHolder {
    public static final String BEAN_NAME = "flinkJobPropertiesHolder";

    private static final Set<FlinkJobProperties> PROPERTIES = new CopyOnWriteArraySet<>();

    public static void registerProperties(FlinkJobProperties properties) {
        PROPERTIES.add(properties);
    }

    public static void registerAllProperties(List<FlinkJobProperties> properties) {
        PROPERTIES.addAll(properties);
    }

    public static List<FlinkJobProperties> getProperties() {
        return new ArrayList<>(PROPERTIES);
    }

}
