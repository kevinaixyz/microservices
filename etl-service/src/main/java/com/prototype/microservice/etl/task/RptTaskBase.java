package com.prototype.microservice.etl.task;

import com.prototype.microservice.etl.constant.AppConstant;
import com.prototype.microservice.etl.data.BatchJobProcessor;
import com.prototype.microservice.etl.meta.ColumnMetaInfo;
import com.prototype.microservice.etl.meta.CommonConfigInfo;
import com.prototype.microservice.etl.meta.OverallConfig;
import com.prototype.microservice.etl.utils.BaseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RptTaskBase {
    protected BatchJobProcessor batchJobProcessor;
    protected OverallConfig overallConfig;

    @Autowired
    public RptTaskBase(OverallConfig overallConfig, BatchJobProcessor batchJobProcessor) {
        this.overallConfig = overallConfig;
        this.batchJobProcessor = batchJobProcessor;
    }

    protected void loadConfig(CommonConfigInfo configInfo) throws Exception {
        List<File> files;
        if (configInfo.getIsLoadAll()) {
            files = BaseHelper.getFiles(configInfo);
        } else {
            LocalDate scheduleFileDate = LocalDate.now();
            if (configInfo.getScheduleDaysOffset() != 0) {
                scheduleFileDate = LocalDate.now().plusDays(configInfo.getScheduleDaysOffset());
            }
            String asOfDate = BaseHelper.formatDate(scheduleFileDate, configInfo.getFileDateFormat() == null ? AppConstant.ISO_DATE_PATTERN : configInfo.getFileDateFormat());
            files = BaseHelper.getFiles(configInfo, asOfDate);
        }

        if (files == null || files.size() == 0) {
            return;
        }
        if (configInfo.getFileType() == null) {
            return;
        }
        for (File file : files) {
            Map<String, String> sysColValMap = genSysColValue(configInfo, file);
            batchJobProcessor.setConfigInfo(configInfo);
            batchJobProcessor.setFile(file);
            batchJobProcessor.setSysColValMap(sysColValMap);
            batchJobProcessor.process();
        }
    }

    private Map<String, String> genSysColValue(CommonConfigInfo configInfo, File file) {
        if (configInfo == null || configInfo.getSystemColumns() == null) {
            return null;
        }
        List<ColumnMetaInfo> sysCols = configInfo.getSystemColumns();
        Map<String, String> map = new HashMap<>();
        for (ColumnMetaInfo sysCol : sysCols) {
            String key = sysCol.getTableColName();
            if (ColumnMetaInfo.SYS_COL_FILE_DATE.equalsIgnoreCase(key)) {
                map.put(key, BaseHelper.getFileDateStr(file, configInfo.getFileDateFormat()));
            } else if (ColumnMetaInfo.SYS_COL_CREATE_DATETIME.equalsIgnoreCase(key)) {
                String createdDate = BaseHelper.getCurrentDatetimeStr();
                map.put(key, createdDate);
            } else if (ColumnMetaInfo.SYS_COL_FILE_NAME.equalsIgnoreCase(key)) {
                map.put(key, file.getName());
            }
        }
        return map;
    }
}
