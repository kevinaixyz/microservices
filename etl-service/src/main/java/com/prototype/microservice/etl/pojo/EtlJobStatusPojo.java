package com.prototype.microservice.etl.pojo;

public class EtlJobStatusPojo {
    String rptJobId;
    String status;
    String resultList;
    String startTime;
    String endTime;


    public String getRptJobId() {
        return rptJobId;
    }

    public void setRptJobId(String rptJobId) {
        this.rptJobId = rptJobId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResultList() {
        return resultList;
    }

    public void setResultList(String resultList) {
        this.resultList = resultList;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
