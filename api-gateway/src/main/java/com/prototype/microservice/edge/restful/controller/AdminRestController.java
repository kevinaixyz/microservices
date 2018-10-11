package com.prototype.microservice.edge.restful.controller;

import java.text.MessageFormat;

import com.prototype.microservice.edge.constant.EdgeServerAppConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.haitong.microservice.commons.json.ResponseJson;
import com.haitong.microservice.commons.json.SimpleResponseJson;

/**
 * RESTful API endpoints for administrative tasks
 *
 *
 *
 */
@RestController
@RequestMapping("/admin")
public class AdminRestController extends BaseRestController {

	final static Logger LOG = LoggerFactory.getLogger(AdminRestController.class);

	/**
	 * Get the list of all Zuul routes per defined in the application.yml
	 *
	 * @return
	 */
	@RequestMapping(value = "/get/routes", method = RequestMethod.GET)
	public ResponseJson getRoutes() {
		SimpleResponseJson resp = new SimpleResponseJson(getAppName());
		resp.setRespCode(EdgeServerAppConstant.JSON_RESP_CODE_OK);
		resp.setRespMsg(MessageFormat.format("Welcome to {0}", new Object[] { getAppName() }));
		return resp;
	}

	/**
	 *
	 *
	 * @return
	 */
	@RequestMapping(value = "/refresh/routes", method = RequestMethod.GET)
	public ResponseJson refreshRoutes() {
		SimpleResponseJson resp = new SimpleResponseJson(getAppName());
		resp.setRespCode(EdgeServerAppConstant.JSON_RESP_CODE_OK);
		resp.setRespMsg(MessageFormat.format("Welcome to {0}", new Object[] { getAppName() }));
		return resp;
	}

}
