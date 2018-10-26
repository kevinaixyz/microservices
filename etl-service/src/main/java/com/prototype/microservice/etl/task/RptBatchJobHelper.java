package com.prototype.microservice.etl.task;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.prototype.microservice.etl.constant.AppConstant;
import com.prototype.microservice.etl.data.BatchJobProcessor;
import com.prototype.microservice.etl.data.ColumnMetaInfo;
import com.prototype.microservice.etl.data.CommonConfigInfo;
import com.prototype.microservice.etl.utils.EtlHelper;

@Component
public class RptBatchJobHelper {
	@Autowired
	BatchJobProcessor batchJobProcessor;
	
	public static boolean isFileNamePatternMatch(String fileNamePattern, CommonConfigInfo configInfo){
		if(configInfo!=null&&configInfo.getFileNamePattern()!=null&&StringUtils.isNotBlank(fileNamePattern)){
			if(configInfo.getFileNamePattern().contains(fileNamePattern)){
				return true;
			}
		}
		return false;
	}
	public void loadConfig(CommonConfigInfo configInfo) throws Exception{
		List<File> files = null;
		if(configInfo.getIsLoadAll()){
			files = EtlHelper.getFiles(configInfo);
		}else{
			String asOfDate = EtlHelper.formatDate(LocalDate.now(), configInfo.getFileDateFormat()==null?AppConstant.ISO_DATE_PATTERN:configInfo.getFileDateFormat());
			files = EtlHelper.getFiles(configInfo, asOfDate);
		}
		
		if(files==null||files.size()==0){
			return;
		}
		if(configInfo.getFileType()==null){
			return;
		}
		for(File file:files){
			String fileDateVal = EtlHelper.getFileDateStr(file, configInfo.getFileDateFormat());
			Map<String, String> sysColValMap = genSysColValue(configInfo, fileDateVal);
			batchJobProcessor.setConfigInfo(configInfo);
			batchJobProcessor.setFile(file);
			batchJobProcessor.setSysColValMap(sysColValMap);
			batchJobProcessor.process();
		}
	}
	public Map<String, String> genSysColValue(CommonConfigInfo configInfo, String fileDate){
		if(configInfo==null||configInfo.getSystemColumns()==null){
			return null;
		}
		List<ColumnMetaInfo> sysCols = configInfo.getSystemColumns();
		Map<String, String> map = new HashMap<String, String>();
		for(ColumnMetaInfo sysCol: sysCols){
			String key = sysCol.getTableColName();
			if(BatchJobProcessor.SYS_COL_FILE_DATE.equalsIgnoreCase(key)){
				map.put(key, fileDate);
			}
			if(BatchJobProcessor.SYS_COL_CRE_DATE.equalsIgnoreCase(key)){
				String createdDate = EtlHelper.getCurrentDateStr();
				map.put(key, createdDate);
			}
		}
		return map;
	}
}
