package com.esflink.starter.configuration;

import com.esflink.starter.constants.BaseEsConstants;
import com.esflink.starter.holder.FlinkJobBus;
import com.esflink.starter.meta.MemoryMetaManager;
import com.esflink.starter.meta.MetaManager;
import com.esflink.starter.properties.EasyFlinkOrdered;
import com.esflink.starter.properties.EasyFlinkProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;

import java.io.File;
import java.util.ServiceLoader;

/**
 * <p> meta 信息管理器 配置类 </p>
 *
 * @author zhouhongyin
 * @since 2023/5/23 15:33
 */
@EnableConfigurationProperties(EasyFlinkProperties.class)
@Configuration
@ConditionalOnProperty(name = BaseEsConstants.ENABLE_PREFIX, havingValue = "true", matchIfMissing = false)
public class MetaManagerConfiguration implements Ordered {

    public static final String SEPARATOR = File.separator;

    @Autowired
    private EasyFlinkProperties easyFlinkProperties;

    @Primary
    @Bean(initMethod = "start", destroyMethod = "stop")
    public MetaManager metaManager() {
        String metaModel = StringUtils.defaultIfBlank(easyFlinkProperties.getMetaModel(), MemoryMetaManager.NAME);

        ServiceLoader<MetaManager> metaManagers = ServiceLoader.load(MetaManager.class);

        MetaManager bean = new MemoryMetaManager();
        for (MetaManager metaManager : metaManagers) {
            String name = metaManager.getName();
            if (metaModel.equals(name)) {
                bean = metaManager;
                break;
            }
        }
        FlinkJobBus.setMetaManager(bean);
        return bean;
    }

    @Override
    public int getOrder() {
        return EasyFlinkOrdered.META_MANAGER;
    }

}
