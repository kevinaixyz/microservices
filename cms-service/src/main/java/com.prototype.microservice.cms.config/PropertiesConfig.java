package com.prototype.microservice.cms.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

public class PropertiesConfig {
    @Value("${cms-service.baseDate}")
    private String baseDate;
    @Value("${cms-service.batch.file.path}")
    private String filePath;
    @Value("${cms-service.batch.file.datePattern}")
    private String datePattern;
    @Value("${cms-service.batch.file.pattern}")
    private String fileNamePattern;
    @Value("${cms-service.batch.file.tradeDateFormat}")
    private String tradeDateFormat;
    @Value("${cms-service.batch.file.titleRowIndex}")
    private int titleRowIndex;
    @Value("${cms-service.batch.file.descnRowIndex}")
    private int descnRowIndex;
    @Value("${cms-service.batch.file.descnColIndex}")
    private int descnColIndex;
    @Value("${cms-service.batch.test.isTest}")
    private boolean isTest;
    @Value("${cms-service.batch.test.systemDate}")
    private String systemDate;
    @Value("${cms-service.batch.test.isContinue}")
    private boolean testIsContinue;

    public String getBaseDate() {
        return baseDate;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getDatePattern() {
        return datePattern;
    }

    public String getFileNamePattern() {
        return fileNamePattern;
    }

    public String getTradeDateFormat() {
        return tradeDateFormat;
    }

    public int getTitleRowIndex() {
        return titleRowIndex;
    }

    public int getDescnRowIndex() {
        return descnRowIndex;
    }

    public int getDescnColIndex() {
        return descnColIndex;
    }

    public boolean isTest() {
        return isTest;
    }

    public String getSystemDate() {
        return systemDate;
    }

    public boolean getTestIsContinue() {
        return testIsContinue;
    }

}
