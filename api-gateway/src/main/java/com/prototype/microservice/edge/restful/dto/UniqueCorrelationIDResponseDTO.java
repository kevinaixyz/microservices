package com.prototype.microservice.edge.restful.dto;

import com.haitong.microservice.commons.json.SimpleResponseJson;

public class UniqueCorrelationIDResponseDTO extends SimpleResponseJson {

	private String generatedCorrelationId;
	private int length;

	public UniqueCorrelationIDResponseDTO(String instanceId) {
		super(instanceId);
	}

	public String getGeneratedCorrelationId() {
		return generatedCorrelationId;
	}

	public void setGeneratedCorrelationId(String generatedCorrelationId) {
		this.generatedCorrelationId = generatedCorrelationId;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
}
