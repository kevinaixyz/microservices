package com.prototype.microservice.commons.helper;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.prototype.microservice.commons.constants.CommonConstants;
import com.prototype.microservice.commons.error.CheckedException;
import com.prototype.microservice.commons.json.BaseJson;
import com.prototype.microservice.commons.json.ErrorResponseJson;
import com.prototype.microservice.commons.json.ResponseJson;
import com.prototype.microservice.commons.json.SimpleResponseJson;

@Component
public class JsonHelper {

    private final static String TIMESTAMP_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZZZ";

    private final static String UNKNOWN_ERROR_MSG = "Unknown error";

    private final static Gson GSON = new Gson();

    @Autowired
    private MessageHelper msgHelper;

    @Autowired
    private CommonHelper comHelper;

    /**
     * Convert JSON string into an object of the specified class.
     *
     * @param json
     * @param classOfT
     * @return
     * @throws Exception
     */
    public <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
        return GSON.fromJson(json, classOfT);
    }

    /**
     * Convert an object into a JSON string.
     *
     * @param src
     * @return
     */
    public String toJson(Object src) {
        return GSON.toJson(src);
    }

    /**
     * Returns an error reponse DTO populated with excepetion message <b>without populating the Correlation ID</b>.
     *
     * @param throwable
     * @param instanceId
     * @param locale
     * @return
     */
    public ErrorResponseJson getErrorResponseJson(final Throwable throwable, final String instanceId, final Locale locale) {
        return getErrorResponseJson(throwable, instanceId, null, locale);
    }

    /**
     * Returns an error reponse DTO populated with excepetion message.
     *
     * @param throwable
     * @param instanceId
     * @param correlationId
     * @param locale
     * @return
     */
    public ErrorResponseJson getErrorResponseJson(final Throwable throwable, final String instanceId, final String correlationId, final Locale locale) {
        ErrorResponseJson resp = new ErrorResponseJson(instanceId);
        if (StringUtils.isBlank(correlationId)) {
            resp.setCorrelationID(CommonConstants.NA);
        } else {
            resp.setCorrelationID(StringUtils.trim(correlationId));
        }
        resp.setRespCode(ErrorResponseJson.JSON_RESP_CODE_ERROR);
        if (throwable != null) {
            if (throwable instanceof CheckedException) {
                CheckedException ce = (CheckedException) throwable;
                resp.setRespSubCode(msgHelper.getCode(ce.getMessage()));
                resp.setRespMsg(msgHelper.getMessage(ce.getMessage(), ce.getArgs(), locale));
            } else if (throwable instanceof ConstraintViolationException) {
                ConstraintViolationException ce = (ConstraintViolationException) throwable;
                Set<ConstraintViolation<?>> violations = ce.getConstraintViolations();
                for (ConstraintViolation<?> violation : violations) {
                    // take the first validation error
                    resp.setRespSubCode(msgHelper.getCode(ConstraintViolation.class.getSimpleName()));
                    if (violation.getConstraintDescriptor().getAnnotation().annotationType().getName().equals(NotNull.class.getName())) {
                        resp.setRespMsg(msgHelper.getMessage(
                                "ex.missingMandatoryField",
                                new String[]{violation.getMessage()},
                                locale));
                    } else if (violation.getConstraintDescriptor().getAnnotation().annotationType().getName().equals(Pattern.class.getName())) {
                        resp.setRespMsg(msgHelper.getMessage(
                                "ex.invalidFormat",
                                new String[]{violation.getMessage()},
                                locale));
                    } else {
                        resp.setRespMsg(msgHelper.getMessage(
                                "ex.validationFailure",
                                new String[]{violation.getMessage()},
                                locale));
                    }
                    break;
                }
            } else {
                resp.setRespMsg(throwable.getLocalizedMessage());
                resp.setRespSubCode(throwable.getClass().getSimpleName());
            }
        } else {
            resp.setRespMsg(UNKNOWN_ERROR_MSG);
        }
        resp.setRespTimestamp(DateFormatUtils.format(System.currentTimeMillis(), TIMESTAMP_PATTERN));
        return resp;
    }

    public BaseJson populateCommonResponseFields(BaseJson json) {
        json.setCorrelationID(comHelper.getJsonCorrelationIDPerRequest());
        if (json instanceof ResponseJson) {
            ((ResponseJson) json).setRespCode(ResponseJson.JSON_RESP_CODE_OK);
            ((ResponseJson) json).setRespTimestamp(comHelper.getLocalisedCurrentTimestampAsString());
        }
        if (json instanceof SimpleResponseJson) {
            ((SimpleResponseJson) json).setInstanceId(comHelper.getAppInstanceName());
        }
        return json;
    }

}
