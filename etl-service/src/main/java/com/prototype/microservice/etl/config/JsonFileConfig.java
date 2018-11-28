package com.prototype.microservice.etl.config;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.prototype.microservice.etl.meta.CommonConfigInfo;
import com.prototype.microservice.etl.meta.OverallConfig;

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

//    public CommonConfigInfo configInfo() {
//        return loadConfig();
//    }

    @Bean("overallConfig")
    OverallConfig overallConfig() {
        return loadOverallConfig();
    }

    private OverallConfig loadOverallConfig() {
        OverallConfig overallConfig = new OverallConfig();
        List<CommonConfigInfo> configList = new ArrayList<>();
        if (StringUtils.isNotBlank(configFilePath)) {
            File directory = new File(configFilePath);
            if (directory.exists() && directory.isDirectory()) {
                File[] configFiles =
                        directory.listFiles((dir, name) -> StringUtils.isBlank(configFileNameKeywords) || (name.contains(configFileNameKeywords) && name.endsWith(".json")));
                if (configFiles == null || configFiles.length == 0) {
                    return null;
                }
                for (File configFile : configFiles) {
                    if (configFile.isFile()) {
                        CommonConfigInfo configInfo = loadConfig(configFile);
                        configList.add(configInfo);
                    }
                }
            }
        }
        overallConfig.setConfigList(configList);
        return overallConfig;
    }

    private CommonConfigInfo loadConfig(File file) {
        CommonConfigInfo configInfo = null;
        Gson gson = new Gson();
        try {
            System.out.println(".....Parsing Config.....");
            JsonReader reader = new JsonReader(new FileReader(file));
            configInfo = gson.fromJson(reader, CommonConfigInfo.class);
            System.setProperty("log.filePath", configInfo.getLogFilePath());
            System.setProperty("log.fileNamePattern", configInfo.getFileNamePattern());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return configInfo;
    }

//    private CommonConfigInfo loadConfig() {
//        File file = new File(configFilePath);
//        return loadConfig(file);
//    }
}
