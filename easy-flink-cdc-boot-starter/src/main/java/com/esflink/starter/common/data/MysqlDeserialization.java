package com.esflink.starter.common.data;

import com.alibaba.fastjson.JSONObject;
import com.esflink.starter.common.data.DataChangeInfo.EventType;
import com.google.common.base.CaseFormat;
import com.ververica.cdc.debezium.DebeziumDeserializationSchema;
import io.debezium.data.Envelope;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.util.Collector;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;

import java.util.List;
import java.util.Optional;

/**
 * mysql消息读取自定义序列化
 *
 * @author zhouhongyin
 * @since 2023/3/5 22:22
 */
public class MysqlDeserialization implements DebeziumDeserializationSchema<DataChangeInfo> {

    public static final String TS_MS = "ts_ms";
    public static final String BIN_FILE = "file";
    public static final String POS = "pos";
    public static final String BEFORE = "before";
    public static final String AFTER = "after";
    public static final String SOURCE = "source";
    public static final EventType READ = EventType.READ;
    public static final EventType CREATE = EventType.CREATE;
    public static final EventType UPDATE = EventType.UPDATE;
    public static final EventType DELETE = EventType.DELETE;

    /**
     * 反序列化数据,转为变更JSON对象
     *
     * @param sourceRecord
     * @param collector
     */
    @Override
    public void deserialize(SourceRecord sourceRecord, Collector<DataChangeInfo> collector) {
        String topic = sourceRecord.topic();
        String[] fields = topic.split("\\.");
        String database = fields[1];
        String tableName = fields[2];
        Struct struct = (Struct) sourceRecord.value();
        final Struct source = struct.getStruct(SOURCE);
        DataChangeInfo dataChangeInfo = new DataChangeInfo();
        dataChangeInfo.setBeforeData(getJsonObject(struct, BEFORE).toJSONString());
        dataChangeInfo.setAfterData(getJsonObject(struct, AFTER).toJSONString());
        //5.获取操作类型  CREATE UPDATE DELETE
        Envelope.Operation operation = Envelope.operationFor(sourceRecord);
        String type = operation.toString().toUpperCase();
        EventType eventType = handleEventType(type);
        dataChangeInfo.setEventType(eventType);
        dataChangeInfo.setFileName(Optional.ofNullable(source.get(BIN_FILE)).map(Object::toString).orElse(""));
        dataChangeInfo.setFilePos(Optional.ofNullable(source.get(POS)).map(x -> Integer.parseInt(x.toString())).orElse(0));
        dataChangeInfo.setDatabase(database);
        dataChangeInfo.setTableName(tableName);
        //Object value = struct.get(TS_MS);
        Object value = source.get(TS_MS);
        dataChangeInfo.setChangeTime(Optional.ofNullable(value).map(x -> Long.parseLong(x.toString())).orElseGet(System::currentTimeMillis));
        //7.输出数据
        collector.collect(dataChangeInfo);
    }

    private EventType handleEventType(String type) {
        EventType eventType = null;
        if (type.equals(CREATE.getName()) || type.equals(READ.getName())) {
            eventType = CREATE;
        } else if (type.equals(UPDATE.getName())) {
            eventType = UPDATE;
        } else if (type.equals(DELETE.getName())) {
            eventType = DELETE;
        }
        return eventType;
    }

    /**
     * 从原始数据获取出变更之前或之后的数据
     *
     * @param value        变更数据
     * @param fieldElement 属性名
     */
    private JSONObject getJsonObject(Struct value, String fieldElement) {
        Struct element = value.getStruct(fieldElement);
        JSONObject jsonObject = new JSONObject();
        if (element != null) {
            Schema afterSchema = element.schema();
            List<Field> fieldList = afterSchema.fields();
            for (Field field : fieldList) {
                Object afterValue = element.get(field);
                jsonObject.put(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, field.name()), afterValue);
            }
        }
        return jsonObject;
    }


    @Override
    public TypeInformation<DataChangeInfo> getProducedType() {
        return TypeInformation.of(DataChangeInfo.class);
    }
}
