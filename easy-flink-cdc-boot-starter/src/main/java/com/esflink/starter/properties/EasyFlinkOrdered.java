package com.esflink.starter.properties;

/**
 * 定义 easy-flink 各模块加载顺序
 *
 * @author zhouhongyin
 * @since 2023/5/23 11:24
 */
public interface EasyFlinkOrdered {

    int PROPERTIES_CONFIG = 10;
    int ZOOKEEPER = 20;
    int META_MANAGER = 30;

    int FLINK_JOB = 60;

}
