package com.prototype.microservice.etl.config;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.prototype.microservice.etl.meta.CommonConfigInfo;
import com.prototype.microservice.etl.meta.OverallConfig;
import com.prototype.microservice.etl.utils.BaseHelper;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;

@Configuration
@DependsOn("overallConfig")
public class RptLoggerConfig {
	@Autowired
	OverallConfig overallConfig;
	
	Map<String, Logger> loggerMap = null;
	
	@PostConstruct
	public void init(){
		
		loggerMap = new HashMap<String, Logger>();
		//Reload:
		if(overallConfig!=null&&overallConfig.getConfigList()!=null){
			for(CommonConfigInfo configInfo:overallConfig.getConfigList()){
//				LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
//				lc.putProperty("log.filePath", configInfo.getLogFilePath());
//				lc.putProperty("log.fileNamePattern", configInfo.getLogFileNamePattern());
				String loggerName = BaseHelper.getLoggerName(configInfo.getFileNamePattern(), BaseHelper.getCurrentDateStr());
				Logger logger = createLogger(loggerName, configInfo.getLogFilePath(), configInfo.getLogFileNamePattern(), Level.DEBUG);
				loggerMap.put(loggerName, logger);
//				Logger logger = lc.getLogger(AppConstant.RPT_LOGGER_NAME);
//				RollingFileAppender rfAppender = (RollingFileAppender)logger.getAppender(AppConstant.RPT_LOGGER_APPENDER_NAME);
//				rfAppender.setContext(lc);
//				TimeBasedRollingPolicy rollingPolicy = (TimeBasedRollingPolicy)rfAppender.getRollingPolicy();
//				rollingPolicy.setFileNamePattern(configInfo.getLogFilePath()+configInfo.getLogFileNamePattern());
//				rfAppender.setRollingPolicy(rollingPolicy);
//				rollingPolicy.start();
//				rfAppender.start();
			}
		}
	}
	
	public Logger createLogger(String name, String path, String fileNamePattern, Level level){
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		
		PatternLayoutEncoder layout = new PatternLayoutEncoder();
	    layout.setContext(loggerContext);
	    String pattern = "[%d{yyyy-MM-dd'T'HH:mm:ss.SSSZ}][%thread][%-5level][%logger{30}] %msg%n";
	    layout.setPattern(pattern);
	    layout.setCharset(Charset.forName("UTF-8"));
	    layout.start();
		
		String appenderName=name+"_Appender";
		RollingFileAppender<ILoggingEvent> rfAppender = new RollingFileAppender<ILoggingEvent>();
		rfAppender.setName(appenderName);
		rfAppender.setEncoder(layout);
		rfAppender.setContext(loggerContext);
		
		TimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new TimeBasedRollingPolicy<ILoggingEvent>();
		rollingPolicy.setFileNamePattern(path+fileNamePattern);
		rollingPolicy.setContext(loggerContext);
		rollingPolicy.setParent(rfAppender);
		rollingPolicy.start();
		
		rfAppender.setRollingPolicy(rollingPolicy);
		rfAppender.start();
		
		Logger logger = (Logger) LoggerFactory.getLogger(name);
		logger.addAppender(rfAppender);
        logger.setLevel(level);
        logger.setAdditive(false);
		return logger;
	}
	
	public Logger getLogger(String loggerName){
		if(loggerMap!=null&&loggerMap.size()>0){
			return loggerMap.get(loggerName);
		}
		return null;
	}
}
