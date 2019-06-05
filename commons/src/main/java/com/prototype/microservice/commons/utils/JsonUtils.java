package com.prototype.microservice.commons.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.prototype.microservice.commons.json.ErrorResponseJson;

public final class JsonUtils {

    private final static String TIMESTAMP_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZZZ";

    private final static String UNKNOWN_ERROR_MSG = "Unknown error";

    private JsonUtils() {
    }

    /**
     * Returns an error reponse DTO populated with excepetion message <b>without populating the Correlation ID</b>.
     *
     * @param throwable
     * @param instanceId
     * @return
     */
    public static ErrorResponseJson getErrorResponseJson(final Throwable throwable, final String instanceId) {
        return getErrorResponseJson(throwable, instanceId, null);
    }

    /**
     * Returns an error reponse DTO populated with excepetion message.
     *
     * @param throwable
     * @param instanceId
     * @param correlationId
     * @return
     */
    public static ErrorResponseJson getErrorResponseJson(final Throwable throwable, final String instanceId, final String correlationId) {
        ErrorResponseJson resp = new ErrorResponseJson(instanceId);
        if (StringUtils.isBlank(correlationId)) {
            resp.setCorrelationID("N/A");
        } else {
            resp.setCorrelationID(StringUtils.trim(correlationId));
        }
        resp.setRespCode(ErrorResponseJson.JSON_RESP_CODE_ERROR);
        if (throwable != null) {
            resp.setRespMsg(throwable.getLocalizedMessage());
            resp.setRespSubCode(throwable.getClass().getSimpleName());
        } else {
            resp.setRespMsg(UNKNOWN_ERROR_MSG);
        }
        resp.setErrorTimestamp(DateFormatUtils.format(System.currentTimeMillis(), TIMESTAMP_PATTERN));
        return resp;
    }

}
