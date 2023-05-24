package com.esflink.starter.config;

import org.springframework.core.io.Resource;

import java.util.List;

/**
 * 获取配置文件
 *
 * @author zhouhongyin
 * @since 2023/5/23 16:48
 */
public interface FlinkPropertiesParser {

    List<FlinkListenerProperties> getProperties(Resource resource);
}
