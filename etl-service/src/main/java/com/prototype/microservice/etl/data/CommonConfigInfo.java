package com.prototype.microservice.etl.data;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class CommonConfigInfo {
	private String fileDateFormat;
	private boolean isLoadAll;
	private String filePath;
	private String fileNamePattern;
	private String logFilePath;
	private String logFileNamePattern;
	private String dbTableName;
	private String colDelimiter;
	private List<String> nullValFilter;
	private String otherInfo;
	private List<ColumnMetaInfo> columns;
	private List<ColumnMetaInfo> systemColumns;
	private String fileType;
	private String fileEncoding;
	private int headerRowIndex=0;
	private int startRowIndex=1;
	private int endRowIndexFromBottom=0;
	
	public static final String FILE_TYPE_CSV="CSV";
	public static final String FILE_TYPE_TXT="TXT";
	public static final String FILE_TYPE_EXCEL="EXCEL";
	
	public boolean getIsLoadAll() {
		return isLoadAll;
	}
	public void setIsLoadAll(boolean isLoadAll) {
		this.isLoadAll = isLoadAll;
	}
	public String getFileDateFormat() {
		return fileDateFormat;
	}
	public void setFileDateFormat(String fileDateFormat) {
		this.fileDateFormat = fileDateFormat;
	}
	public String getFileNamePattern() {
		return fileNamePattern;
	}
	public void setFileNamePattern(String fileNamePattern) {
		this.fileNamePattern = fileNamePattern;
	}
	public String getLogFilePath() {
		return logFilePath;
	}
	public void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
	}
	public String getLogFileNamePattern() {
		return logFileNamePattern;
	}
	public void setLogFileNamePattern(String logFileNamePattern) {
		this.logFileNamePattern = logFileNamePattern;
	}
	public String getColDelimiter() {
		return colDelimiter;
	}
	public void setColDelimiter(String colDelimiter) {
		this.colDelimiter = colDelimiter;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getDbTableName() {
		return dbTableName;
	}
	public void setDbTableName(String dbTableName) {
		this.dbTableName = dbTableName;
	}
	public String getOtherInfo() {
		return otherInfo;
	}
	public void setOtherInfo(String otherInfo) {
		this.otherInfo = otherInfo;
	}
	public List<ColumnMetaInfo> getColumns() {
		return columns;
	}
	public void setColumns(List<ColumnMetaInfo> columns) {
		this.columns = columns;
	}
	public List<ColumnMetaInfo> getSystemColumns() {
		return systemColumns;
	}
	public void setSystemColumns(List<ColumnMetaInfo> systemColumns) {
		this.systemColumns = systemColumns;
	}
	public List<String> getNullValFilter() {
		return nullValFilter;
	}
	public void setNullValFilter(List<String> nullValFilter) {
		this.nullValFilter = nullValFilter;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	public int getStartRowIndex() {
		return startRowIndex;
	}
	public void setStartRowIndex(int startRowIndex) {
		this.startRowIndex = startRowIndex;
	}
	public int getEndRowIndexFromBottom() {
		return endRowIndexFromBottom;
	}
	public void setEndRowIndexFromBottom(int endRowIndexFromBottom) {
		this.endRowIndexFromBottom = endRowIndexFromBottom;
	}
	public int getHeaderRowIndex() {
		return headerRowIndex;
	}
	public void setHeaderRowIndex(int headerRowIndex) {
		this.headerRowIndex = headerRowIndex;
	}
	public String getFileEncoding() {
		return fileEncoding;
	}
	public void setFileEncoding(String fileEncoding) {
		this.fileEncoding = fileEncoding;
	}
}
