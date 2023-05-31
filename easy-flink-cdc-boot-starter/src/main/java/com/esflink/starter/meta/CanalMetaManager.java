package com.esflink.starter.meta;

/**
 * meta信息管理器
 *
 * @author zhouhongyin
 * @since 2023/5/31 23:14
 */
public interface CanalMetaManager {


    /**
     * 获取 cursor 游标
     */
    LogPosition getCursor(FlinkJobIdentity clientIdentity) ;

    /**
     * 更新 cursor 游标
     */
    void updateCursor(FlinkJobIdentity clientIdentity, LogPosition position);

}
