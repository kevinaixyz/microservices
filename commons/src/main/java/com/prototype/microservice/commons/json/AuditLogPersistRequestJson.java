package com.prototype.microservice.commons.json;

import org.apache.commons.lang3.StringUtils;

import com.prototype.microservice.commons.constants.CommonConstants;

public class AuditLogPersistRequestJson extends RequestJson {

	private static final long serialVersionUID = -4328641405526664194L;
	private String appId;
	private String userId;
	private String preCondition;
	private String postCondition;
	private String logMessage;
	private String logTimestamp;

	public AuditLogPersistRequestJson() { }

	public String getAppId() {
		if (StringUtils.isBlank(appId)) {
			return CommonConstants.NA;
		} else {
			return appId;
		}
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getUserId() {
		if (StringUtils.isBlank(userId)) {
			return CommonConstants.NA;
		} else {
			return userId;
		}
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPreCondition() {
		if (StringUtils.isBlank(preCondition)) {
			return CommonConstants.NA;
		} else {
			return preCondition;
		}
	}

	public void setPreCondition(String preCondition) {
		this.preCondition = preCondition;
	}

	public String getPostCondition() {
		if (StringUtils.isBlank(postCondition)) {
			return CommonConstants.NA;
		} else {
			return postCondition;
		}
	}

	public void setPostCondition(String postCondition) {
		this.postCondition = postCondition;
	}

	public String getLogMessage() {
		if (StringUtils.isBlank(logMessage)) {
			return CommonConstants.NA;
		} else {
			return logMessage;
		}
	}

	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}

	public String getLogTimestamp() {
		return logTimestamp;
	}

	public void setLogTimestamp(String logTimestamp) {
		this.logTimestamp = logTimestamp;
	}

}
