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
public class FlinkJobSinkTableFilter implements FlinkJobSinkFilter {

    public static boolean filter(FlinkJobSink sink, DataChangeInfo dataChangeInfo) {
        FlinkSink annotation = sink.getClass().getAnnotation(FlinkSink.class);
        if (annotation == null) return false;

        String[] tableNames = annotation.table();
        if (ObjectUtils.isEmpty(tableNames)) return true;

        String dataChangeInfoDatabase = dataChangeInfo.getDatabase();
        String dataChangeInfoTableName = dataChangeInfo.getTableName();

        // 过滤数据库
        for (String tableName : tableNames) {
            String[] split = tableName.split("\\.");

            if(dataChangeInfoDatabase.equalsIgnoreCase("dbo")){
                dataChangeInfoDatabase = "dbo";
            }
            if (dataChangeInfoDatabase.equals(split[0])) {
                if ("*".equals(split[1]) || dataChangeInfoTableName.equals(split[1])) {
                    return true;
                }
            }

        }
        return false;
    }
}
