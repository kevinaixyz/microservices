package com.prototype.microservice.etl.data;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.prototype.microservice.etl.utils.EtlHelper;

public abstract class RptActionProcessor {
	@Autowired
	protected SqlAssembler sqlAssembler;
	@Autowired
	protected RptRepository rptRepository;
	protected Logger rptLogger;
	protected CommonConfigInfo configInfo;
	protected RptFileParser rptFileParser;
	@Autowired
	ExcelFileParser excelFileParser;
	@Autowired
	CsvFileParser csvFileParser;
	public CommonConfigInfo getConfigInfo() {
		return configInfo;
	}

	public void setConfigInfo(CommonConfigInfo configInfo) {
		this.configInfo = configInfo;
		if(configInfo!=null){
			if(CommonConfigInfo.FILE_TYPE_EXCEL.equalsIgnoreCase(configInfo.getFileType())){
				rptFileParser = excelFileParser;
			}else if(CommonConfigInfo.FILE_TYPE_CSV.equalsIgnoreCase(configInfo.getFileType())){
				rptFileParser = csvFileParser;
			}else if(CommonConfigInfo.FILE_TYPE_TXT.equalsIgnoreCase(configInfo.getFileType())){
				rptFileParser = csvFileParser;
			}
			rptFileParser.setConfigInfo(configInfo);
			rptLogger = LoggerFactory.getLogger(EtlHelper.getLoggerName(configInfo.getFileNamePattern(), EtlHelper.getCurrentDateStr()));
		}
	}

	public boolean isDataExist(String tableName, List<ColumnMetaInfo> columns, List<String> values) {
		String sql = sqlAssembler.countDataByColumns(tableName, columns, values);
		if(StringUtils.isNotBlank(sql)){
			BigDecimal recNo = rptRepository.execCount(sql);
			rptLogger.info(MessageFormat.format("[{0}] Check data exist...: {1}", new Object[]{this.getClass().getName(),sql}));
			rptLogger.info(MessageFormat.format("[{0}] There is/are {1} record(s) exist.", new Object[]{this.getClass().getName(),recNo}));
			if(recNo.compareTo(BigDecimal.ZERO)>0){
				return true;
			}
		}
		return false;
	}

	public void deleteDataByColumns(String tableName, List<ColumnMetaInfo> columns, List<String> values) {
		String sql = sqlAssembler.genDeleteSqlByDate(tableName, columns, values);
		if(StringUtils.isNotBlank(sql)){
			int recNo = rptRepository.execUpdate(sql);
			rptLogger.info(MessageFormat.format("[{0}] Delete data...: there is/are {1} record(s) deleted.", new Object[]{this.getClass().getName(),recNo}));
		}
	}
	
	public abstract int process() throws Exception;
}
