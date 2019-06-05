package com.prototype.microservice.edge.restful.controller;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.prototype.microservice.commons.json.ErrorResponseJson;
import com.prototype.microservice.commons.utils.JsonUtils;

/**
 * The framework level controller for error handling
 */
@RestControllerAdvice
public class RestErrorController extends BaseRestController {

    private static final Logger LOG = LoggerFactory.getLogger(RestErrorController.class);

    @Value("${spring.application.name:esb-core}")
    private String appName;

    /**
     * Generate a generic error response.
     *
     * @param throwable
     * @param model
     * @return
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseJson handleException(final Throwable throwable, final Model model) {
        if (throwable != null) {
            LOG.error(MessageFormat.format("Error occurred in REST controller: {0}", throwable.getLocalizedMessage()),
                    throwable);
        } else {
            LOG.error(MessageFormat.format("Error occurred in REST controller: {0}", "Unknown error!"));
        }
        return JsonUtils.getErrorResponseJson(throwable, getInstanceId());
    }

}
