package com.prototype.microservice.commons.helper;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

import com.google.gson.JsonSyntaxException;
import com.prototype.microservice.commons.error.CheckedException;
import com.prototype.microservice.commons.error.CommonErrorConstant;
import com.prototype.microservice.commons.json.ErrorResponseJson;

/**
 * Error handling utils.
 *
 *
 *
 */
@Component
public class ErrorHelper {

	private final static Logger LOG = LoggerFactory.getLogger(ErrorHelper.class);

	private ErrorHelper() { }

	@Autowired
	private JsonHelper jsonHelper;

	public void handleErrorResponse(Exception e) throws CheckedException {

		LOG.error(MessageFormat.format(
				"handleErrorResponse -> clazz[{0}] message[{1}]",
				new Object[] { e.getClass().getName(), e.getLocalizedMessage() }), e);

		if (e instanceof HttpServerErrorException) {
			HttpServerErrorException httpE = (HttpServerErrorException) e;
			ErrorResponseJson errorJson;
			try {
				errorJson = jsonHelper.fromJson(httpE.getResponseBodyAsString(), ErrorResponseJson.class);
			} catch (JsonSyntaxException jsonEx) {
				throw new CheckedException(CommonErrorConstant.OPERATION_FAILURE, new Object[] { jsonEx.getLocalizedMessage() });
			}
			LOG.error(MessageFormat.format(
					"handleErrorResponse -> HttpServerErrorException -> message: {0}",
					new Object[] { jsonHelper.toJson(errorJson) }), httpE);
			throw new CheckedException(CommonErrorConstant.GENERIC, new Object[] { errorJson.getRespMsg() });
		} else {
			throw new CheckedException(CommonErrorConstant.OPERATION_FAILURE, new Object[] { e.getLocalizedMessage() });
		}
	}

}
