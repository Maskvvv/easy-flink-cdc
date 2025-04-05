package com.esflink.starter.common.data;


import java.io.Serializable;

/**
 * 数据变更对象
 *
 * @author zhouhongyin
 * @since 2023/3/5 22:23
 */
public class DataChangeInfo implements Serializable {
    /**
     * 变更前数据
     */
    private String beforeData;
    /**
     * 变更后数据
     */
    private String afterData;
    /**
     * 变更类型 1新增 2修改 3删除
     */
    private EventType eventType;
    /**
     * binlog文件名
     */
    private String fileName;
    /**
     * binlog当前读取点位
     */
    private Integer filePos;
    /**
     * 数据库名
     */
    private String database;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 变更时间
     */
    private Long changeTime;

    public String getBeforeData() {
        return beforeData;
    }

    public void setBeforeData(String beforeData) {
        this.beforeData = beforeData;
    }

    public String getAfterData() {
        return afterData;
    }

    public void setAfterData(String afterData) {
        this.afterData = afterData;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getFilePos() {
        return filePos;
    }

    public void setFilePos(Integer filePos) {
        this.filePos = filePos;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Long getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Long changeTime) {
        this.changeTime = changeTime;
    }

    @Override
    public String toString() {
        return "DataChangeInfo{" +
                "beforeData='" + beforeData + '\'' +
                ", afterData='" + afterData + '\'' +
                ", eventType=" + eventType +
                ", fileName='" + fileName + '\'' +
                ", filePos=" + filePos +
                ", database='" + database + '\'' +
                ", tableName='" + tableName + '\'' +
                ", changeTime=" + changeTime +
                '}';
    }

    public enum EventType {
        READ("READ", 0),
        CREATE("CREATE", 1),
        UPDATE("UPDATE", 2),
        DELETE("DELETE", 3),
        ;
        private String name;
        private Integer value;


        EventType(String name, Integer value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public Integer getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "EventType{" +
                    "name='" + name + '\'' +
                    ", value=" + value +
                    '}';
        }
    }
}
