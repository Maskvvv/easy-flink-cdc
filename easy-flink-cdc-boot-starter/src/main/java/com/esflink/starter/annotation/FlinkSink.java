package com.esflink.starter.annotation;

import com.esflink.starter.properties.FlinkJobProperties;
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

    /**
     * <p>Flink Job Name</p>
     *
     * @see FlinkJobProperties#getName()
     */
    String value();

    /**
     * <p>数据库名 {@link FlinkJobProperties#getDatabaseList()}</p>
     * <p>不指定则为 {@link FlinkJobProperties} 中指定的数据库</p>
     * <p>example: {database1,database2}</p>
     */
    String[] database() default {};

    /**
     * <p>数据库表名 {@link FlinkJobProperties#getTableList()}</p>
     * <p>不指定则为 {@link FlinkJobProperties} 中指定的据库表</p>
     * <p>
     * example: {database1.table1,database2.*}, 可以通过通配符 {@code *} 标识接受 database2 库下所有表的数据变更
     * </p>
     */
    String[] table() default {};
}
