package com.esflink.starter.properties.parser;

import com.esflink.starter.properties.FlinkJobProperties;
import com.typesafe.config.Config;
import org.springframework.core.io.Resource;

import java.util.List;

/**
 * 获取配置文件
 *
 * @author zhouhongyin
 * @since 2023/5/23 16:48
 */
public interface FlinkListenerPropertiesParser {

    default List<FlinkJobProperties> parse(Resource resource) {
        throw new RuntimeException();
    }

    default List<FlinkJobProperties> parse(Config config) {
        throw new RuntimeException();
    }

    String getName();
}
