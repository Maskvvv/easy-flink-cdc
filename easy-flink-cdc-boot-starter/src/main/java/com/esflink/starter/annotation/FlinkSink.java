package com.esflink.starter.annotation;

import com.esflink.starter.register.FlinkScannerRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 全局 sink 扫描注解
 *
 * @author zhouhongyin
 * @since 2023/5/23 11:17
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(FlinkScannerRegister.class)
public @interface FlinkSink {
    String value() default "";
}
