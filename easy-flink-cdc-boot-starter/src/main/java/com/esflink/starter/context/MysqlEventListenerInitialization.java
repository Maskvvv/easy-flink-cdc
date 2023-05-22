package com.esflink.starter.context;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * 初始化 EventListener
 *
 * @author zhouhongyin
 * @since 2023/5/22 23:14
 */
@Component
public class MysqlEventListenerInitialization implements InitializingBean {

    /**
     * 创建 Listener
     * 获取 sink
     */
    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
