package com.esflink.starter.prox;

import com.esflink.starter.utils.LogUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 代理类
 *
 * @author zhouhongyin
 * @since 2023/5/28 15:12
 */
public class FlinkSinkProxy implements InvocationHandler {
    private Object sink;

    public FlinkSinkProxy(Object sink) {
        this.sink = sink;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = method.invoke(sink, args);

        LogUtils.info("我是代理类" + sink.getClass().getName());
        return result;
    }

}
