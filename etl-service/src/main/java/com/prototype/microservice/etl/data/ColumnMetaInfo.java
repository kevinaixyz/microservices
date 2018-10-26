package com.prototype.microservice.etl.data;

import org.springframework.stereotype.Component;

@Component
public class ColumnMetaInfo {
	public final static String DB_TYPE_DATE="date";
	public final static String DB_TYPE_TIMESTAMP="timestamp";
	public final static String DB_TYPE_CHAR="char";
	public final static String DB_TYPE_NUMBER="number";
	
	public final static String JAVA_TYPE_INTEGER="integer";
	public final static String JAVA_TYPE_DOUBLE="double";
	public final static String JAVA_TYPE_BIGDECIMAL="bigdecimal";
	
	private String tableColName;
	private String colName;
	private String type;
	private String format;
	private int colIndex;
	private int startIndex;
	private int endIndex;
	
	
	public String getTableColName() {
		return tableColName;
	}
	public void setTableColName(String tableColName) {
		this.tableColName = tableColName;
	}
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	public int getColIndex() {
		return colIndex;
	}
	public void setColIndex(int colIndex) {
		this.colIndex = colIndex;
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
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public int getEndIndex() {
		return endIndex;
	}
	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}
}
