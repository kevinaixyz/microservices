package com.prototype.microservice.etl.response;

import java.util.List;

import com.prototype.microservice.commons.json.SimpleResponseJson;

public class FileUploadResponseJson extends SimpleResponseJson {

    /**
     *
     */
    private static final long serialVersionUID = 1L;


    private String jobId;
    private String jobStatus;
    private String mode;
    private List<Integer> processedRecNumList;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<Integer> getProcessedRecNumList() {
        return processedRecNumList;
    }

    public void setProcessedRecNumList(List<Integer> processedRecNumList) {
        this.processedRecNumList = processedRecNumList;
    }
}
