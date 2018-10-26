package com.prototype.microservice.etl.data;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.prototype.microservice.commons.error.CheckedException;
import com.prototype.microservice.etl.utils.EtlHelper;

public abstract class RptFileParser {
	protected final static String SYS_COL_FILE_DATE="FILE_DATE";
	protected final static String SYS_COL_CRE_DATE="CRE_DATE";
	protected InputStream in;
	protected int currentRowIndex=0;
	protected List<Integer> columnIndices;
	protected CommonConfigInfo configInfo;
	protected Charset charset;
	protected Logger rptLogger;
	@Autowired
	protected EtlHelper rptHelper;
	@Autowired
	protected SqlAssembler sqlAssembler;
	@Autowired
	protected RptRepository rptRepository;
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
		this.rptLogger = LoggerFactory.getLogger(EtlHelper.getLoggerName(configInfo.getFileNamePattern(), EtlHelper.getCurrentDateStr()));
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
	
	public int parseRows(Map<String, String> sysColValMap) throws Exception {
		int totalRowNum=0;
		columnIndices = getColumnIndices();
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
				Map<String, String> sysColVal = null;
				sysColVal = genSysColVal(configInfo.getSystemColumns(), sysColValMap);	
//				if(sysColValMap!=null){
//				}
//				else{
//					sysColVal = genSysColVal(configInfo.getSystemColumns());
//				}
				execNativeSql(configInfo.getDbTableName(), configInfo.getColumns(), valueStrList, sysColVal);
				currentRowIndex++;
				totalRowNum++;
			} catch(CheckedException e){
				rptLogger.error(e.getMessage());
				throw e;
			} catch (Exception e) {
				rptLogger.error(MessageFormat.format("{0}Parse line error at line number:{1}",
						new Object[] { "[RptFileParser]", (currentRowIndex + 1) }));
				rptLogger.error(e.getMessage());
				e.printStackTrace();
				continue;
			}
		}
		return totalRowNum;
	}
	public Map<String, String> genSysColVal(List<ColumnMetaInfo> sysColInfo, Map<String,String> colValMap){
		Map<String, String> map = new HashMap<String, String>();
		if(sysColInfo==null||colValMap==null||sysColInfo.size()!=colValMap.size()){
			return map;
		}
		for(int i=0;i<sysColInfo.size();i++){
			String key = sysColInfo.get(i).getTableColName();
			String sqlVal = null;
			if(colValMap.containsKey(key)){
				sqlVal = RptColumnParser.parseColumnValueForNative(colValMap.get(key), sysColInfo.get(i), configInfo.getNullValFilter());
			}
			map.put(key, sqlVal);
		}
		return map;
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
