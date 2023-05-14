package com.ptu.entity;


import lombok.Data;

@Data
public class HbaseInfo {
    private String rowKey;
    private String columnFamily;
    private String column;
    private String value;
    private String time;

    public HbaseInfo(String rowKey, String columnFamily, String column, String value, String time) {
        this.rowKey = rowKey;
        this.columnFamily = columnFamily;
        this.column = column;
        this.value = value;
        this.time = time;
    }
}
