package com.prototype.microservice.etl.task;


import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.prototype.microservice.etl.meta.CommonConfigInfo;
import com.prototype.microservice.etl.meta.OverallConfig;
import com.prototype.microservice.etl.utils.BaseHelper;

@Component
public class P125ScheduledTasks {
	private static final Logger logger = LoggerFactory.getLogger(P125ScheduledTasks.class);
	@Value("${p125.fireprice.fileNamePattern}")
	private String p125FirepriceFileNamePattern;
	@Value("${p125.fireprice.schedule-job.immediate:true}")
	private boolean p125FireImmediate;
	@Value("${p125.fireprice.schedule-job.cron}")
	private String p125CronSchedule;
	@Autowired
	private BaseHelper rptHelper;
	@Autowired
	EtlBatchJobHelper etlBatchJobHelper;
	
	@PostConstruct
	public void init() {
		logger.info("Application properties:");
		logger.info("DataSource properties:");
		logger.info("p125.report.immediate={}", p125FireImmediate);
		logger.info("p125.report.cron={}", p125CronSchedule);
		if(p125FireImmediate){
			logger.info("Executing startup schedule - P125 Report");
			fetchExcelData();
			logger.info("Completed startup schedule - P125 Report");
		}else {
			logger.info("Ignoring startup schedule - P125 Report");
		}
	}

	@Scheduled(cron = "${p125.fireprice.schedule-job.cron}")
	public void fetchExcelData() {

		logger.info("Processing fireprice data");
		System.out.println("==============Read fireprice Data===========");

		OverallConfig overallConfig = rptHelper.getOverallConfig();
		if (overallConfig != null && overallConfig.getConfigList() != null
				&& overallConfig.getConfigList().size() > 0) {

			for (CommonConfigInfo configInfo : overallConfig.getConfigList()) {
				try {
					if(EtlBatchJobHelper.isFileNamePatternMatch(p125FirepriceFileNamePattern, configInfo)){
						etlBatchJobHelper.loadConfig(configInfo);
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
