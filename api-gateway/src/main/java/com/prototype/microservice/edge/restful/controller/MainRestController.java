package com.prototype.microservice.edge.restful.controller;

import java.text.MessageFormat;

import com.prototype.microservice.edge.constant.EdgeServerAppConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.prototype.microservice.commons.json.ResponseJson;
import com.prototype.microservice.commons.json.SimpleResponseJson;

@RestController
public class MainRestController extends BaseRestController {

	final static Logger LOG = LoggerFactory.getLogger(MainRestController.class);

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseJson home() {
		SimpleResponseJson resp = new SimpleResponseJson(getAppName());
		resp.setRespCode(EdgeServerAppConstant.JSON_RESP_CODE_OK);
		resp.setRespMsg(MessageFormat.format("Welcome to {0}", new Object[] { getAppName() }));
		return resp;
	}



}
