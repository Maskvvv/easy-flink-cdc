package com.esflink.starter.holder;

import com.esflink.starter.annotation.FlinkSink;
import com.esflink.starter.common.data.DataChangeInfo;
import com.esflink.starter.common.data.FlinkJobSink;
import org.springframework.util.ObjectUtils;

/**
 * <p> flink sink 是否接收数据变更消息过滤器 </p>
 *
 * @author zhouhongyin
 * @since 2023/6/14 9:33
 */
public class FlinkJobSinkDatabaseFilter implements FlinkJobSinkFilter {

    public static boolean filter(FlinkJobSink sink, DataChangeInfo dataChangeInfo) {
        FlinkSink annotation = sink.getClass().getAnnotation(FlinkSink.class);
        if (annotation == null) return false;

        String[] databases = annotation.database();

        if (ObjectUtils.isEmpty(databases)) return true;

        // 过滤数据库
        for (String database : databases) {
            if (database.equals(dataChangeInfo.getDatabase()) || dataChangeInfo.getDatabase().equalsIgnoreCase("dbo")) {
                return FlinkJobSinkTableFilter.filter(sink, dataChangeInfo);
            }

        }
        return false;
    }
}
