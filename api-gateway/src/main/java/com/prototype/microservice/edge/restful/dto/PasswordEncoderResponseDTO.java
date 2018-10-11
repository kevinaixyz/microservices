package com.prototype.microservice.edge.restful.dto;

import com.haitong.microservice.commons.json.SimpleResponseJson;

public class PasswordEncoderResponseDTO extends SimpleResponseJson {

	private String encodedPassword;
	private String rawPassword;
	private String encoder;

	public PasswordEncoderResponseDTO(String instanceId) {
		super(instanceId);
	}

	public String getEncodedPassword() {
		return encodedPassword;
	}

	public void setEncodedPassword(String encodedPassword) {
		this.encodedPassword = encodedPassword;
	}

	public String getRawPassword() {
		return rawPassword;
	}

	public void setRawPassword(String rawPassword) {
		this.rawPassword = rawPassword;
	}

	public String getEncoder() {
		return encoder;
	}

	public void setEncoder(String encoder) {
		this.encoder = encoder;
	}

}
