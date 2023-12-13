package com.esflink.starter.register;

import com.esflink.starter.annotation.EasyFlinkScan;
import com.esflink.starter.common.utils.EEVersionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.esflink.starter.constants.BaseEsConstants.ENABLE_BANNER;
import static com.esflink.starter.constants.BaseEsConstants.ENABLE_PREFIX;

/**
 * easy-flink 注册器
 *
 * @author zhouhongyin
 * @since 2023/5/23 11:12
 */
public class FlinkScannerRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {
    private static final Logger logger = LoggerFactory.getLogger(FlinkScannerRegister.class);

    private Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Boolean enable = Optional.ofNullable(environment.getProperty(ENABLE_PREFIX)).map(Boolean::parseBoolean).orElse(Boolean.TRUE);
        if (!enable) {
            logger.info("===> Easy Flink-CDC is not enabled");
            return;
        }


        boolean banner = Optional.ofNullable(environment.getProperty(ENABLE_BANNER)).map(Boolean::parseBoolean).orElse(Boolean.TRUE);
        if (banner) {
            String versionStr = EEVersionUtils.getJarVersion(this.getClass());
            System.out.println("\n" +
                    "\n" +
                    " ______                  ______ _ _       _            _____ _____   _____ \n" +
                    "|  ____|                |  ____| (_)     | |          / ____|  __ \\ / ____|\n" +
                    "| |__   __ _ ___ _   _  | |__  | |_ _ __ | | ________| |    | |  | | |     \n" +
                    "|  __| / _` / __| | | | |  __| | | | '_ \\| |/ /______| |    | |  | | |     \n" +
                    "| |___| (_| \\__ \\ |_| | | |    | | | | | |   <       | |____| |__| | |____ \n" +
                    "|______\\__,_|___/\\__, | |_|    |_|_|_| |_|_|\\_\\       \\_____|_____/ \\_____|\n" +
                    "                  __/ |                                                    \n" +
                    "                 |___/                                                     " +
                    "\n------------------------------------------------------>"
            );
            System.out.println(":: version   :: " + versionStr);
            System.out.println(":: home      :: https://github.com/Maskvvv/easy-flink-cdc");
        }

        AnnotationAttributes mapperScanAttrs = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(EasyFlinkScan.class.getName()));
        if (mapperScanAttrs != null) {
            registerBeanDefinitions(mapperScanAttrs, registry);
        }
    }

    void registerBeanDefinitions(AnnotationAttributes annoAttrs, BeanDefinitionRegistry registry) {
        ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
        List<String> basePackages = new ArrayList<>();
        basePackages.addAll(
                Arrays.stream(annoAttrs.getStringArray("value"))
                        .filter(StringUtils::hasText)
                        .collect(Collectors.toList()));

        scanner.registerFilters();
        scanner.doScan(StringUtils.toStringArray(basePackages));
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
