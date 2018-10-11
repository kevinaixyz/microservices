package com.prototype.microservice.commons.json;

public class SimpleResponseJson extends ResponseJson {

	private static final long serialVersionUID = 3709303770050814201L;
	private String instanceId;

	public SimpleResponseJson() {}

	/**
	 * Construct the DTO object given an Instance ID of the resource provider (i.e.:
	 * the microservice that generate this response)
	 *
	 * @param instanceId
	 */
	public SimpleResponseJson(String instanceId) {
		super();
		this.instanceId = instanceId;
	}

	/**
	 * Returns the Instance ID of the resource provider (i.e.: the microservice that
	 * generate this response)
	 *
	 * @return
	 */
	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

}
