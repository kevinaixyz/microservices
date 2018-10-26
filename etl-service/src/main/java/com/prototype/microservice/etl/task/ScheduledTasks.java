package com.prototype.microservice.etl.task;


import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import com.prototype.microservice.etl.data.CommonConfigInfo;
import com.prototype.microservice.etl.data.RptOverallConfig;
import com.prototype.microservice.etl.utils.EtlHelper;

//@Component
public class ScheduledTasks {
	private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
	
	@Value("${etl.report.immediate:true}")
	private boolean fireImmediate = true;
	@Value("${p125.report.immediate:true}")
	private boolean p125FireImmediate = true;
	@Value("${etl.report.cron}")
	private String cronSchedule;
	@Value("${p125.report.cron}")
	private String p125CronSchedule;
	@Autowired
	private EtlHelper rptHelper;
	@Autowired
	RptBatchJobHelper rptBatchJobHelper;
	public ScheduledTasks() {
	}
	
	@PostConstruct
	public void init() {
		logger.info("Application properties:");
		logger.info("etl.report.immediate={}", fireImmediate);
		logger.info("etl.report.cron={}", cronSchedule);
		logger.info("DataSource properties:");
		logger.info("p125.report.immediate={}", p125FireImmediate);
		logger.info("p125.report.cron={}", p125CronSchedule);

		if (fireImmediate) {
			logger.info("Executing startup schedule - etl Report");
			fetchCsvTxtData();
			logger.info("Completed startup schedule - etl Report");
		} else {
			logger.info("Ignoring startup schedule - etl Report");
		}
		if(p125FireImmediate){
			logger.info("Executing startup schedule - P125 Report");
			fetchExcelData();
			logger.info("Completed startup schedule - P125 Report");
		}else {
			logger.info("Ignoring startup schedule - P125 Report");
		}
	}

	@Scheduled(cron = "${etl.report.cron}")
	public void fetchCsvTxtData() {
		logger.info("Migrating TR data");
		System.out.println("==============Read CSV/TXT Data===========");

		RptOverallConfig overallConfig = rptHelper.getOverallConfig();
		if (overallConfig != null && overallConfig.getConfigList() != null
				&& overallConfig.getConfigList().size() > 0) {
			for (CommonConfigInfo configInfo : overallConfig.getConfigList()) {
				try {
					if(configInfo.getFileType()==null||CommonConfigInfo.FILE_TYPE_CSV.equalsIgnoreCase(configInfo.getFileType())||CommonConfigInfo.FILE_TYPE_TXT.equalsIgnoreCase(configInfo.getFileType())){
						rptBatchJobHelper.loadConfig(configInfo);						
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
					logger.error(e.getMessage());
					e.printStackTrace();
					continue;
				}
			}
		}
	}
	@Scheduled(cron = "${p125.report.cron}")
	public void fetchExcelData() {

		logger.info("Migrating TR data");
		System.out.println("==============Read Excel Data===========");

		RptOverallConfig overallConfig = rptHelper.getOverallConfig();
		if (overallConfig != null && overallConfig.getConfigList() != null
				&& overallConfig.getConfigList().size() > 0) {

			for (CommonConfigInfo configInfo : overallConfig.getConfigList()) {
				try {
					if (configInfo.getFileType() != null
							&& CommonConfigInfo.FILE_TYPE_EXCEL.equalsIgnoreCase(configInfo.getFileType())) {
						rptBatchJobHelper.loadConfig(configInfo);
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
					logger.error(e.getMessage());
					e.printStackTrace();
					continue;
				}
			}
		}
	}
	
}
