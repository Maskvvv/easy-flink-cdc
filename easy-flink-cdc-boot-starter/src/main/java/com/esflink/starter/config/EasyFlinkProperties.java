package com.esflink.starter.config;


import com.esflink.starter.constants.BaseEsConstants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * flink 数据库配置信息
 *
 * @author zhouhongyin
 * @since 2023/3/5 22:28
 */
@Configuration
@ConfigurationProperties(value = BaseEsConstants.PREFIX)
@ConditionalOnProperty(name = BaseEsConstants.ENABLE_PREFIX, havingValue = "true", matchIfMissing = true)
public class EasyFlinkProperties {

    private String enable;


}
