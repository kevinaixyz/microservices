package com.prototype.microservice.etl.json;

import com.prototype.microservice.commons.json.RequestJson;

public class BaseRequestJson extends RequestJson{

	private String requestedUserID;

	public void setRequestedUserID(String requestedUserID) {
		this.requestedUserID = requestedUserID;
	}

	public String getRequestedUserID() {
		return requestedUserID;
	}

}
