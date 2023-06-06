package com.esflink.starter.configuration;

import com.esflink.starter.constants.BaseEsConstants;
import com.esflink.starter.meta.FileMixedMetaManager;
import com.esflink.starter.properties.EasyFlinkOrdered;
import com.esflink.starter.properties.EasyFlinkProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

import java.io.File;

/**
 * meta 信息管理器 配置类
 *
 * @author zhouhongyin
 * @since 2023/5/23 15:33
 */
@EnableConfigurationProperties(EasyFlinkProperties.class)
@Configuration
@ConditionalOnProperty(name = BaseEsConstants.ENABLE_PREFIX, havingValue = "true", matchIfMissing = true)
public class MetaManagerConfiguration implements Ordered, EnvironmentAware {

    public static final String SEPARATOR = File.separator;

    @Autowired
    private EasyFlinkProperties easyFlinkProperties;

    private Environment environment;

    @Primary
    @Bean(initMethod = "start", destroyMethod = "stop")
    public FileMixedMetaManager fileMixedMetaManager() {
        FileMixedMetaManager fileMixedMetaManager = new FileMixedMetaManager();
        EasyFlinkProperties.Meta meta = easyFlinkProperties.getMeta();
        fileMixedMetaManager.setDataDir(meta.getDataDir());
        return fileMixedMetaManager;
    }

    @Override
    public int getOrder() {
        return EasyFlinkOrdered.ORDER_ZOOKEEPER;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
