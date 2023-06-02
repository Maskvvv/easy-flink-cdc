package com.esflink.starter.meta;

import com.esflink.starter.common.exception.EasyFlinkCDCException;

/**
 * @author zhouhongyin
 * @since 2023/6/2 15:31
 */
public abstract class AbstractMetaLifeCycle implements MetaLifeCycle {

    protected volatile boolean running = false; // 是否处于运行中

    public boolean isStart() {
        return running;
    }

    public void start() {
        if (running) {
            throw new EasyFlinkCDCException(this.getClass().getName() + " has startup , don't repeat start");
        }

        running = true;
    }

    public void stop() {
        if (!running) {
            throw new EasyFlinkCDCException(this.getClass().getName() + " isn't start , please check");
        }

        running = false;
    }

}
