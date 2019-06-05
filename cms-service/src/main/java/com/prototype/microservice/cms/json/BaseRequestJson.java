package com.prototype.microservice.cms.json;

import com.prototype.microservice.commons.json.RequestJson;

public class BaseRequestJson extends RequestJson {

    private String requestedUserID;

    public void setRequestedUserID(String requestedUserID) {
        this.requestedUserID = requestedUserID;
    }

    public String getRequestedUserID() {
        return requestedUserID;
    }

}
