package com.prototype.microservice.etl.task;


import com.prototype.microservice.etl.data.BatchJobProcessor;
import com.prototype.microservice.etl.meta.CommonConfigInfo;
import com.prototype.microservice.etl.meta.OverallConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ScheduledTasks extends RptTaskBase {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    @Value("${etl.warrant.schedule-job.immediate:true}")
    private boolean fireImmediate = true;
    @Value("${etl.warrant.schedule-job.cron}")
    private String cronSchedule;

    public ScheduledTasks(OverallConfig overallConfig, BatchJobProcessor batchJobProcessor) {
        super(overallConfig, batchJobProcessor);
    }

    @PostConstruct
    public void init() {
        logger.info("Application properties:");
        logger.info("schedule.immediate={}", fireImmediate);
        logger.info("schedule.cron={}", cronSchedule);

        if (fireImmediate) {
            logger.info("Executing startup schedule - etl Report");
            fetchData();
            logger.info("Completed startup schedule - etl Report");
        } else {
            logger.info("Ignoring startup schedule - etl Report");
        }
    }

    @Scheduled(cron = "${schedule.cron}")
    public void fetchData() {
        System.out.println("==============Read CSV/TXT Data===========");
        if (overallConfig != null && overallConfig.getConfigList() != null
                && overallConfig.getConfigList().size() > 0) {
            for (CommonConfigInfo configInfo : overallConfig.getConfigList()) {
                try {
                    loadConfig(configInfo);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    logger.error(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

}
