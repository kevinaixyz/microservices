package com.prototype.microservice.etl.task;


import javax.annotation.PostConstruct;

import com.prototype.microservice.etl.constant.AppConstant;
import com.prototype.microservice.etl.data.*;
import com.prototype.microservice.etl.meta.ColumnMetaInfo;
import com.prototype.microservice.etl.meta.CommonConfigInfo;
import com.prototype.microservice.etl.meta.OverallConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import com.prototype.microservice.etl.utils.BaseHelper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ScheduledTasks {
	private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
	
	@Value("${etl.immediate:true}")
	private boolean fireImmediate = true;
	@Value("${etl.cron}")
	private String cronSchedule;

	private BatchJobProcessor batchJobProcessor;
	private OverallConfig overallConfig;

	@Autowired
	public ScheduledTasks(OverallConfig overallConfig, BatchJobProcessor batchJobProcessor) {
		this.overallConfig = overallConfig;
		this.batchJobProcessor = batchJobProcessor;
	}
	
	@PostConstruct
	public void init() {
		logger.info("Application properties:");
		logger.info("etl.report.immediate={}", fireImmediate);
		logger.info("etl.report.cron={}", cronSchedule);

		if (fireImmediate) {
			logger.info("Executing startup schedule - etl Report");
			fetchData();
			logger.info("Completed startup schedule - etl Report");
		} else {
			logger.info("Ignoring startup schedule - etl Report");
		}
	}

	@Scheduled(cron = "${etl.report.cron}")
	public void fetchData() {
		System.out.println("==============Read CSV/TXT Data===========");
		if (overallConfig != null && overallConfig.getConfigList() != null
				&& overallConfig.getConfigList().size() > 0) {
			for (CommonConfigInfo configInfo : overallConfig.getConfigList()) {
				try {
					if(configInfo.getFileType()==null||
							CommonConfigInfo.FILE_TYPE_CSV.equalsIgnoreCase(configInfo.getFileType())||
							CommonConfigInfo.FILE_TYPE_TXT.equalsIgnoreCase(configInfo.getFileType())){
						loadConfig(configInfo);
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
					logger.error(e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}
	//@Scheduled(cron = "${p125.report.cron}")
	public void fetchExcelData() {
		System.out.println("==============Read Excel Data===========");
		if (overallConfig != null && overallConfig.getConfigList() != null
				&& overallConfig.getConfigList().size() > 0) {

			for (CommonConfigInfo configInfo : overallConfig.getConfigList()) {
				try {
					if (configInfo.getFileType() != null
							&& CommonConfigInfo.FILE_TYPE_EXCEL.equalsIgnoreCase(configInfo.getFileType())) {
						loadConfig(configInfo);
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
					logger.error(e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}

	public boolean isFileNamePatternMatch(String fileNamePattern, CommonConfigInfo configInfo){
		if(configInfo!=null&&configInfo.getFileNamePattern()!=null&& StringUtils.isNotBlank(fileNamePattern)){
			return configInfo.getFileNamePattern().contains(fileNamePattern);
		}
		return false;
	}

	private void loadConfig(CommonConfigInfo configInfo) throws Exception{
		List<File> files;
		if(configInfo.getIsLoadAll()){
			files = BaseHelper.getFiles(configInfo);
		}else{
			String asOfDate = BaseHelper.formatDate(LocalDate.now(), configInfo.getFileDateFormat()==null? AppConstant.ISO_DATE_PATTERN:configInfo.getFileDateFormat());
			files = BaseHelper.getFiles(configInfo, asOfDate);
		}

		if(files==null||files.size()==0){
			return;
		}
		if(configInfo.getFileType()==null){
			return;
		}
		for(File file:files){
			Map<String, String> sysColValMap = genSysColValue(configInfo, file);
			batchJobProcessor.setConfigInfo(configInfo);
			batchJobProcessor.setFile(file);
			batchJobProcessor.setSysColValMap(sysColValMap);
			batchJobProcessor.process();
		}
	}
	private Map<String, String> genSysColValue(CommonConfigInfo configInfo, File file){
		if(configInfo==null||configInfo.getSystemColumns()==null){
			return null;
		}
		List<ColumnMetaInfo> sysCols = configInfo.getSystemColumns();
		Map<String, String> map = new HashMap<>();
		for(ColumnMetaInfo sysCol: sysCols){
			String key = sysCol.getTableColName();
			if(BatchJobProcessor.SYS_COL_FILE_DATE.equalsIgnoreCase(key)){
				map.put(key, BaseHelper.getFileDateStr(file, configInfo.getFileDateFormat()));
			} else if(BatchJobProcessor.SYS_COL_CRE_DATE.equalsIgnoreCase(key)){
				String createdDate = BaseHelper.getCurrentDateStr();
				map.put(key, createdDate);
			} else if(BatchJobProcessor.SYS_COL_FILE_NAME.equalsIgnoreCase(key)){
                map.put(key, file.getName());
            }
		}
		return map;
	}
}
