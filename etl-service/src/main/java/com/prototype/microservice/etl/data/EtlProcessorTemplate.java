package com.prototype.microservice.etl.data;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import com.prototype.microservice.etl.meta.ColumnMetaInfo;
import com.prototype.microservice.etl.meta.CommonConfigInfo;
import com.prototype.microservice.etl.repository.EtlCommonRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.prototype.microservice.etl.utils.BaseHelper;

public abstract class EtlProcessorTemplate {
	@Autowired
	protected SqlAssembler sqlAssembler;
	@Autowired
	protected EtlCommonRepository etlCommonRepository;
	protected Logger rptLogger;
	protected CommonConfigInfo configInfo;
	protected EtlFileParser etlFileParser;
	@Autowired
	EtlFileParser excelFileParser;
	@Autowired
	CsvFileParser csvFileParser;
	public CommonConfigInfo getConfigInfo() {
		return configInfo;
	}

	public void setConfigInfo(CommonConfigInfo configInfo) {
		this.configInfo = configInfo;
		if(configInfo!=null){
			if(CommonConfigInfo.FILE_TYPE_EXCEL.equalsIgnoreCase(configInfo.getFileType())){
				etlFileParser = excelFileParser;
			}else if(CommonConfigInfo.FILE_TYPE_CSV.equalsIgnoreCase(configInfo.getFileType())){
				etlFileParser = csvFileParser;
			}else if(CommonConfigInfo.FILE_TYPE_TXT.equalsIgnoreCase(configInfo.getFileType())){
				etlFileParser = csvFileParser;
			}
			etlFileParser.setConfigInfo(configInfo);
			rptLogger = LoggerFactory.getLogger(BaseHelper.getLoggerName(configInfo.getFileNamePattern(), BaseHelper.getCurrentDateStr()));
		}
	}

	boolean isDataExist(String tableName, List<ColumnMetaInfo> columns, List<String> values) {
		String sql = null;
		if(columns.size()==values.size()){
			sql = sqlAssembler.countDataByColumns(tableName, columns, values);
		}

		if(StringUtils.isNotBlank(sql)){
			BigDecimal recNo = etlCommonRepository.execCount(sql);
			rptLogger.info(MessageFormat.format("[{0}] Check data exist...: {1}", this.getClass().getName(),sql));
			rptLogger.info(MessageFormat.format("[{0}] There is/are {1} record(s) exist.", this.getClass().getName(),recNo));
			return recNo.compareTo(BigDecimal.ZERO)>0;
		}
		return false;
	}

	void deleteDataByColumns(String tableName, List<ColumnMetaInfo> columns, List<String> values) {
		String sql = sqlAssembler.genDeleteSqlByDate(tableName, columns, values);
		if(StringUtils.isNotBlank(sql)){
			int recNo = etlCommonRepository.execUpdate(sql);
			rptLogger.info(MessageFormat.format("[{0}] Delete data...: there is/are {1} record(s) deleted.", this.getClass().getName(),recNo));
		}
	}

	public void checkDataExistByFileName(Map<String, String> sysColValMap){
		if(configInfo==null||configInfo.getSystemColumns()==null){
			return;
		}
		if(sysColValMap!=null){
			List<ColumnMetaInfo> sysColumns = configInfo.getSystemColumns();
			List<ColumnMetaInfo> checkColumns = new ArrayList<>();
			List<String> checkVals = new ArrayList<>();
			if(sysColValMap.containsKey(ColumnMetaInfo.SYS_COL_FILE_NAME)){
				IntStream.range(0, sysColumns.size()).forEach(i->{
					if(ColumnMetaInfo.SYS_COL_FILE_NAME.equalsIgnoreCase(sysColumns.get(i).getTableColName())){
						checkColumns.add(sysColumns.get(i));
						checkVals.add(sysColValMap.get(ColumnMetaInfo.SYS_COL_FILE_NAME));
					}
				});
			}
			boolean isExist = isDataExist(configInfo.getDbTableName(), checkColumns, checkVals);
			if(isExist){
				deleteDataByColumns(configInfo.getDbTableName(), checkColumns, checkVals);
			}
		}
	}
	
	public abstract int process() throws Exception;
}
