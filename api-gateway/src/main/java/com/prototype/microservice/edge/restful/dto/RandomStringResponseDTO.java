package com.prototype.microservice.edge.restful.dto;

import com.haitong.microservice.commons.json.SimpleResponseJson;

public class RandomStringResponseDTO extends SimpleResponseJson {

	private String type;
	private String random;
	private int length;

	public RandomStringResponseDTO(String instanceId) {
		super(instanceId);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRandom() {
		return random;
	}

	public void setRandom(String random) {
		this.random = random;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

}
