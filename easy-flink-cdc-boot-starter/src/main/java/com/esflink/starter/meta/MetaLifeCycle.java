package com.esflink.starter.meta;

import java.io.Serializable;

/**
 * @author jianghang 2012-7-12 上午09:39:33
 * @version 1.0.0
 */
public interface MetaLifeCycle extends Serializable {

    void start();

    void stop();

    boolean isStart();
}
