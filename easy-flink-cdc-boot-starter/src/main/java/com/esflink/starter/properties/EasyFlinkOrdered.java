package com.esflink.starter.properties;

/**
 * 定义 easy-flink 各模块加载顺序
 *
 * @author zhouhongyin
 * @since 2023/5/23 11:24
 */
public interface EasyFlinkOrdered {
    int ORDER_SINK = 10;
    int ORDER_ZOOKEEPER = 15;
    int ORDER_META_MANAGER = 20;
    int ORDER_CONF = 20;
    int ORDER_LISTENER_PROPERTIES = 30;
    int ORDER_LISTENER = 60;
    int ORDER_REDIS = 40;
    int ORDER_ROCKETMQ = 40;
}
