package com.prototype.microservice.etl.meta;

import org.springframework.stereotype.Component;

@Component
public class ColumnMetaInfo {
    public final static String DB_TYPE_DATE = "date";
    public final static String DB_TYPE_TIMESTAMP = "timestamp";
    public final static String DB_TYPE_CHAR = "char";
    public final static String DB_TYPE_NUMBER = "number";

    public final static String JAVA_TYPE_INTEGER = "integer";
    public final static String JAVA_TYPE_DOUBLE = "double";
    public final static String JAVA_TYPE_BIGDECIMAL = "bigdecimal";

    public final static String SYS_COL_FILE_DATE = "FILE_DATE";
    public final static String SYS_COL_FILE_NAME = "FILE_NAME";
    public final static String SYS_COL_CREATE_DATETIME = "CREATE_DATETIME";

    private String tableColName;
    private String fileColName;
    private String type;
    private String format;

    public String getTableColName() {
        return tableColName;
    }

    public void setTableColName(String tableColName) {
        this.tableColName = tableColName;
    }

    public String getFileColName() {
        return fileColName;
    }

    public void setFileColName(String fileColName) {
        this.fileColName = fileColName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
