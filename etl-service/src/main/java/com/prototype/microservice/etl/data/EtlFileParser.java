package com.prototype.microservice.etl.data;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.prototype.microservice.etl.meta.ColumnMetaInfo;
import com.prototype.microservice.etl.meta.CommonConfigInfo;
import com.prototype.microservice.etl.repository.EtlCommonRepository;
import com.prototype.microservice.etl.utils.EtlMessageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.prototype.microservice.commons.error.CheckedException;
import com.prototype.microservice.etl.utils.BaseHelper;

public abstract class EtlFileParser {
	protected final static String SYS_COL_FILE_DATE="FILE_DATE";
	protected final static String SYS_COL_CRE_DATE="CRE_DATE";
	protected InputStream in;
	protected int currentRowIndex=0;
	protected List<Integer> columnIndices;
	protected CommonConfigInfo configInfo;
	protected Charset charset;
	protected Logger rptLogger;
	@Autowired
	protected BaseHelper baseHelper;
	@Autowired
	protected SqlAssembler sqlAssembler;
	@Autowired
	protected EtlCommonRepository etlCommonRepository;
	@Autowired
	protected EtlMessageHelper msgHelper;
	protected abstract void init();
	protected abstract void clean();
	protected abstract Charset getFileEncoding();
	protected abstract List<Integer> getColumnIndices() throws Exception;
	protected abstract List<String> parseColumnValues(Object row, List<ColumnMetaInfo> columns) throws Exception;
	protected abstract void execNativeSql(String dbTableName, List<ColumnMetaInfo> columns, List<String> valueStrList, Map<String, String> sysColValMap) throws Exception;
	protected abstract boolean isBlankRow(Object row);
	protected abstract boolean hasNextRow();
	protected abstract Object getRow();
	protected Map<String, String> sysColValMap;
	
	public InputStream getIn() {
		return in;
	}
	public void setIn(InputStream in) {
		this.in = in;
	}
	public Map<String, String> getSysColValMap() {
		return sysColValMap;
	}
	public void setSysColValMap(Map<String, String> sysColValMap) {
		this.sysColValMap = sysColValMap;
	}
	public CommonConfigInfo getConfigInfo() {
		return configInfo;
	}
	public void setConfigInfo(CommonConfigInfo configInfo) {
		this.configInfo = configInfo;
		this.rptLogger = LoggerFactory.getLogger(BaseHelper.getLoggerName(configInfo.getFileNamePattern(), BaseHelper.getCurrentDateStr()));
	}
	public Logger getRptLogger() {
		return rptLogger;
	}
	public void setRptLogger(Logger rptLogger) {
		this.rptLogger = rptLogger;
	}
	
	public int parseFile() throws Exception{
		init();
		int recNum = parseRows(sysColValMap);
		clean();
		return recNum;
	}
	
	private int parseRows(Map<String, String> sysColValMap) throws Exception {
		int totalRowNum=0;
		this.columnIndices = getColumnIndices();
		while (hasNextRow()) {
			if (currentRowIndex < configInfo.getStartRowIndex()) { // skip the first line (table header)
				currentRowIndex++;
				continue;
			}
			try {
				Object row=getRow();
				if(isBlankRow(row)){
					currentRowIndex++;
					continue;
				}
				List<String> valueStrList = parseColumnValues(row, configInfo.getColumns());
				Map<String, String> sysColVal = genSysColVal(configInfo.getSystemColumns(), sysColValMap);
				//execNativeSql(configInfo.getDbTableName(), configInfo.getColumns(), valueStrList, sysColVal);
				List<ColumnMetaInfo> allCols = new ArrayList<>();
				List<String> allValues=new ArrayList<>();
				allValues.addAll(valueStrList);
				for(ColumnMetaInfo sysCol: configInfo.getSystemColumns()){
					allValues.add(sysColValMap.get(sysCol.getTableColName()));
				}
				allCols.addAll(configInfo.getColumns());
				allCols.addAll(configInfo.getSystemColumns());
				execParamSql(configInfo.getDbTableName(),allCols,allValues);
				currentRowIndex++;
				totalRowNum++;
			} catch(CheckedException e){
				rptLogger.error(e.getMessage());
				throw e;
			} catch (Exception e) {
				rptLogger.error(MessageFormat.format("{0}Parse line error at line number:{1}",
						"[EtlFileParser]", (currentRowIndex + 1)));
				rptLogger.error(e.getMessage());
				e.printStackTrace();

			}
		}
		return totalRowNum;
	}
	private Map<String, String> genSysColVal(List<ColumnMetaInfo> sysColInfo, Map<String, String> colValMap){
		Map<String, String> map = new HashMap<>();
		if(sysColInfo==null||colValMap==null||sysColInfo.size()!=colValMap.size()){
			return map;
		}
		for (ColumnMetaInfo aSysColInfo : sysColInfo) {
			String key = aSysColInfo.getTableColName();
			String sqlVal = null;
			if (colValMap.containsKey(key)) {
				sqlVal = EtlColumnParser.parseColumnValueForNative(colValMap.get(key), aSysColInfo, configInfo.getNullValFilter());
			}
			map.put(key, sqlVal);
		}
		return map;
	}
	private Map<String, Object> genSysColValForParam(List<ColumnMetaInfo> sysColInfo, Map<String, String> colValMap){
		Map<String, Object> map = new HashMap<>();
		if(sysColInfo==null||colValMap==null||sysColInfo.size()!=colValMap.size()){
			return map;
		}
		for (ColumnMetaInfo aSysColInfo : sysColInfo) {
			String key = aSysColInfo.getTableColName();
			Object value = null;
			if (colValMap.containsKey(key)) {
				value = EtlColumnParser.parseColumnForParam(colValMap.get(key), aSysColInfo);
			}
			map.put(key, value);
		}
		return map;
	}
	public String execParamSql(String tableName,  List<ColumnMetaInfo> columnsInfo, List<String> values){
		String sql = sqlAssembler.genInsertSqlWithParam(tableName, columnsInfo);
		List<Object> valueObjList = sqlAssembler.parseValues(columnsInfo, values);
		etlCommonRepository.insertDataByParams(sql, valueObjList);
		return sql;
	}
//	public Map<String, String> genSysColVal(List<ColumnMetaInfo> sysColInfo){
//		Map<String, String> map = new HashMap<String, String>();
//		for(ColumnMetaInfo sysCol: sysColInfo){
//			String key = sysCol.getTableColName();
//			if(SYS_COL_FILE_DATE.equalsIgnoreCase(key)){
//				String fileDate = configInfo.getExecOnDate();
//				if(StringUtils.isNotBlank(fileDate)){
//					fileDate = RptHelper.formatLocalDate(fileDate, configInfo.getFileDateFormat());					
//				}else{
//					fileDate = RptHelper.formatLocalDate(LocalDate.now(), configInfo.getFileDateFormat());
//				}
//				String sqlVal = RptHelper.dateFieldToNativeSql(fileDate, AppConstant.ISO_DATE_PATTERN);
//				map.put(key, sqlVal);
//			}
//			if(SYS_COL_CRE_DATE.equalsIgnoreCase(key)){
//				String createdDate = RptHelper.getCurrentDateStr();
//				String sqlVal = RptHelper.dateFieldToNativeSql(createdDate, AppConstant.ISO_DATE_PATTERN);
//				map.put(key, sqlVal);
//			}
//		}
//		return map;
//	}
}
