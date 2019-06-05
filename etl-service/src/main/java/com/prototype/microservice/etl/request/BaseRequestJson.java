package com.prototype.microservice.etl.request;

import com.prototype.microservice.commons.json.RequestJson;

public class BaseRequestJson extends RequestJson {

    private String requestedUserID;
    private String requestTimestamp;

    public void setRequestedUserID(String requestedUserID) {
        this.requestedUserID = requestedUserID;
    }

    public String getRequestedUserID() {
        return requestedUserID;
    }

    public String getRequestTimestamp() {
        return requestTimestamp;
    }

    public void setRequestTimestamp(String requestTimestamp) {
        this.requestTimestamp = requestTimestamp;
    }
}
