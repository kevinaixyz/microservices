package com.prototype.microservice.etl.controller;

import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;

import com.prototype.microservice.commons.json.ResponseJson;
import com.prototype.microservice.commons.json.SimpleResponseJson;

public class BaseRestController {

	private static final Logger LOG = LoggerFactory.getLogger(BaseRestController.class);

	@Value("${eureka.instance.metadataMap.instanceId:UNKNOWN_EUREKA_CLIENT_INSTANCE}")
	private String instanceId;

	public BaseRestController() {
		super();

	}
	protected String getInstanceId() {
		return StringUtils.trimToEmpty(instanceId);
	}

	@RequestMapping("/")
	public ResponseJson home() {
		final SimpleResponseJson resp = new SimpleResponseJson(getInstanceId());
		resp.setRespCode(ResponseJson.JSON_RESP_CODE_OK);
		resp.setRespMsg(MessageFormat.format("Welcome to {0}", instanceId));
		return resp;
	}

}
