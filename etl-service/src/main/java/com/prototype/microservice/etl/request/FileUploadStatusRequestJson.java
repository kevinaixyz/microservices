package com.prototype.microservice.etl.request;


public class FileUploadStatusRequestJson extends BaseRequestJson {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String jobId;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

}
