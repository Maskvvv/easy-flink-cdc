package com.esflink.starter.prox;

import com.esflink.starter.common.data.DataChangeInfo;
import com.esflink.starter.common.utils.LogUtils;
import com.esflink.starter.meta.FlinkJobIdentity;
import com.esflink.starter.meta.LogPosition;
import com.esflink.starter.meta.MetaManager;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 代理类
 *
 * @author zhouhongyin
 * @since 2023/5/28 15:12
 */
public class FlinkSinkProxy implements InvocationHandler, Serializable {

    private transient final MetaManager metaManager;
    private transient final FlinkJobIdentity flinkJobIdentity;

    public FlinkSinkProxy(MetaManager metaManager, FlinkJobIdentity flinkJobIdentity) {
        this.metaManager = metaManager;
        this.flinkJobIdentity = flinkJobIdentity;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!"invoke".equals(method.getName())) return null;

        DataChangeInfo dataChangeInfo = (DataChangeInfo) args[0];

        LogPosition logPosition = new LogPosition();
        logPosition.setFlinkJobIdentity(flinkJobIdentity);
        logPosition.setStartupTimestampMillis(dataChangeInfo.getChangeTime());
        metaManager.updateCursor(flinkJobIdentity, logPosition);
        LogUtils.info("flink job: " + flinkJobIdentity.getFlinkJobName() + ", update on " + dataChangeInfo.getChangeTime());

        return null;
    }

}
