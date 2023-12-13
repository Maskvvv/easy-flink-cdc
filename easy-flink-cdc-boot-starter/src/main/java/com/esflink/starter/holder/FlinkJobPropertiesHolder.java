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

    public static final FlinkJobPropertiesHolder EMPTY = new FlinkJobPropertiesHolder();

    private final Set<FlinkJobProperties> PROPERTIES = new CopyOnWriteArraySet<>();

    public void register(FlinkJobProperties properties) {
        PROPERTIES.add(properties);
    }

    public void registerAll(List<FlinkJobProperties> properties) {
        PROPERTIES.addAll(properties);
    }

    public synchronized void registerAllIfNotAbsent(List<FlinkJobProperties> properties) {
        for (FlinkJobProperties property : properties) {
            if (PROPERTIES.contains(property)) {
                continue;
            }
            PROPERTIES.add(property);
        }
    }

    public List<FlinkJobProperties> getProperties() {
        return new ArrayList<>(PROPERTIES);
    }

}
