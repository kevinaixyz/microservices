package com.prototype.microservice.commons.event;

import org.springframework.context.ApplicationEvent;

import java.sql.Timestamp;

public class AuditLogEvent extends ApplicationEvent {

	private static final long serialVersionUID = 3203004545930674173L;

	private final String appId;
	private final String userId;
	private final String preCondition;
	private final String postCondition;
	private final String logMessage;
	private final Timestamp logTimestamp;

	public AuditLogEvent(Object source,
			String appId,
			String userId,
			String preCondition,
			String postCondition,
			String logMessage,
			Timestamp logTimestamp) {
		super(source);
		this.appId = appId;
		this.userId = userId;
		this.preCondition = preCondition;
		this.postCondition = postCondition;
		this.logMessage = logMessage;
		this.logTimestamp = logTimestamp;
	}

	public String getAppId() {
		return appId;
	}

	public String getUserId() {
		return userId;
	}

	public String getPreCondition() {
		return preCondition;
	}

	public String getPostCondition() {
		return postCondition;
	}

	public String getLogMessage() {
		return logMessage;
	}

	public Timestamp getLogTimestamp() {
		return logTimestamp;
	}

	@Override
	public String toString() {
		return String.format("AuditLogEvent [appId=%s, userId=%s, preCondition=%s, postCondition=%s, logMessage=%s, logTimestamp=%s]",
                appId, userId, preCondition, postCondition, logMessage, logTimestamp);
	}

}
