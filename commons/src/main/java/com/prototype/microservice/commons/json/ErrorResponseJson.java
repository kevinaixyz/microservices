package com.prototype.microservice.commons.json;

import org.apache.commons.lang3.StringUtils;

import com.prototype.microservice.commons.constants.CommonConstants;

public class ErrorResponseJson extends SimpleResponseJson {

    private static final long serialVersionUID = -3810474283957907144L;
    private String errorTimestamp;

    public ErrorResponseJson(final String instanceId) {
        super(instanceId);
    }

    public String getErrorTimestamp() {
        return errorTimestamp;
    }

    public void setErrorTimestamp(String errorTimestamp) {
        this.errorTimestamp = errorTimestamp;
    }

    @Override
    public String getRespMsg() {
        if (StringUtils.isBlank(super.getRespMsg())) {
            return CommonConstants.NA;
        } else {
            return super.getRespMsg();
        }
    }

}
