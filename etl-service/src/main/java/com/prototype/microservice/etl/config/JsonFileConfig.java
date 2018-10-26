package com.prototype.microservice.etl.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.prototype.microservice.etl.data.CommonConfigInfo;
import com.prototype.microservice.etl.data.RptOverallConfig;

@Configuration
public class JsonFileConfig {
	@Value("${config.configFilePath}")
	private String configFilePath;
	@Value("${config.configFileNameKeywords}")
	private String configFileNameKeywords;
	
	public String getConfigFilePath() {
		return configFilePath;
	}
	public void setConfigFilePath(String configFilePath) {
		this.configFilePath = configFilePath;
	}
	
	public CommonConfigInfo configInfo(){
		return loadConfig();
	}
	@Bean("overallConfig")
	RptOverallConfig overallConfig(){
		return loadOverallConfig();
	}
	public RptOverallConfig loadOverallConfig(){
		RptOverallConfig overallConfig = new RptOverallConfig();
		List<CommonConfigInfo> configList = new ArrayList<CommonConfigInfo>();
		if(StringUtils.isNotBlank(configFilePath)){
			File directory = new File(configFilePath);
			if(directory.exists()&&directory.isDirectory()){
				File[] configFiles = directory.listFiles((dir, name)->{
					if(StringUtils.isBlank(configFileNameKeywords)||(name.contains(configFileNameKeywords)&&name.endsWith(".json"))){
						return true;
					}
					return false;
				});
				for(File configFile:configFiles){
					if(configFile.isFile()){
						CommonConfigInfo configInfo = loadConfig(configFile);
						configList.add(configInfo);
					}
				}
			}
		}
		overallConfig.setConfigList(configList);
		return overallConfig;
	}
	public CommonConfigInfo loadConfig(File file){
		CommonConfigInfo configInfo = null;
		Gson gson = new Gson();
		try {
			System.out.println(".....Load Config.....");
			JsonReader reader = new JsonReader(new FileReader(file));
			configInfo = gson.fromJson(reader, CommonConfigInfo.class);
			System.setProperty("log.filePath", configInfo.getLogFilePath());
			System.setProperty("log.fileNamePattern", configInfo.getFileNamePattern());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e1){
			e1.printStackTrace();
		}
		return configInfo;
	}
	public CommonConfigInfo loadConfig(){
		CommonConfigInfo configInfo = null;
		Gson gson = new Gson();
		try {
			System.out.println(".....Load Config.....");
			JsonReader reader = new JsonReader(new FileReader(configFilePath));
			configInfo = gson.fromJson(reader, CommonConfigInfo.class);
			System.setProperty("log.filePath", configInfo.getLogFilePath());
			System.setProperty("log.fileNamePattern", configInfo.getFileNamePattern());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e1){
			e1.printStackTrace();
		}
		return configInfo;
	}
}
