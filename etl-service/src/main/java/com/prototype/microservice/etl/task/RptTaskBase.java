package com.haitong.ficc.tasks;

import com.haitong.microservice.ficc.data.CommonConfigInfo;
import com.haitong.microservice.ficc.data.RptOverallConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RptTaskBase {
    private RptBatchJobHelper rptBatchJobHelper;
    private RptOverallConfig overallConfig;

    @Autowired
    public void RptTaskBase(RptOverallConfig overallConfig){
        this.overallConfig = overallConfig;
    }
    protected void loadConfigByFileNamePattern(String fileNamePtn) throws Exception {
        if (overallConfig != null && overallConfig.getConfigList() != null
                && overallConfig.getConfigList().size() > 0) {
            for (CommonConfigInfo configInfo : overallConfig.getConfigList()) {
                if(RptBatchJobHelper.isFileNamePatternMatch(fileNamePtn, configInfo)){
                    rptBatchJobHelper.loadConfig(configInfo);
                }
            }
        }
    }

    @Autowired
    private void setRptBatchJobHelper(RptBatchJobHelper rptBatchJobHelper){
        this.rptBatchJobHelper = rptBatchJobHelper;
    }
}
