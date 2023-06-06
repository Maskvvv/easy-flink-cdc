package com.esflink.starter.meta;

import java.io.Serializable;

/**
 * meta 信息管理器
 *
 * @author zhouhongyin
 * @since 2023/5/31 23:14
 */
public interface MetaManager extends MetaLifeCycle, Serializable {


    /**
     * 获取 cursor 游标
     */
    LogPosition getCursor(FlinkJobIdentity flinkJobIdentity);

    /**
     * 更新 cursor 游标
     */
    void updateCursor(FlinkJobIdentity flinkJobIdentity, LogPosition position);

}
