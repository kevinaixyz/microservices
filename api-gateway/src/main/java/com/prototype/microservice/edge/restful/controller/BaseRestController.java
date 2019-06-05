package com.prototype.microservice.edge.restful.controller;

import com.prototype.microservice.edge.constant.EdgeServerAppConstant;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.prototype.microservice.commons.json.ErrorResponseJson;
import com.prototype.microservice.commons.json.ResponseJson;

public class BaseRestController {

    private final static Logger LOG = LoggerFactory.getLogger(BaseRestController.class);

    @Value("${spring.application.name:UNKNOWN APPLICATION}")
    private String appName;

    @Value("${eureka.instance.metadataMap.instanceId:UNKNOWN INSTANCE}")
    private String instanceId;

    private final static String UNKNOWN_ERROR_MSG = "Unknown error";

    public BaseRestController() {
        super();
    }

    protected String getInstanceId() {
        return StringUtils.trimToEmpty(instanceId);
    }

    protected String getAppName() {
        return StringUtils.trimToEmpty(appName);
    }

    protected ResponseJson getErrorResponseDTO(Exception e) {
        ErrorResponseJson resp = new ErrorResponseJson(getAppName());
        resp.setRespCode(EdgeServerAppConstant.JSON_RESP_CODE_ERROR);
        if (e != null) {
            resp.setRespMsg(e.getLocalizedMessage());
            resp.setRespSubCode(e.getClass().getSimpleName());
        } else {
            resp.setRespMsg(UNKNOWN_ERROR_MSG);
        }
        resp.setErrorTimestamp(DateFormatUtils.format(System.currentTimeMillis(), EdgeServerAppConstant.TIMESTAMP_PATTERN));
        return resp;
    }

}
