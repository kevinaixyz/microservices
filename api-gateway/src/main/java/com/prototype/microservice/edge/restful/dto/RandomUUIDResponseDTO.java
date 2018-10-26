package com.prototype.microservice.edge.restful.dto;

import com.prototype.microservice.commons.json.SimpleResponseJson;

public class RandomUUIDResponseDTO extends SimpleResponseJson {

	private String uuid;

	public RandomUUIDResponseDTO(String instanceId) {
		super(instanceId);
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUuid() {
		return uuid;
	}
}
