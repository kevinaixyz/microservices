package com.prototype.microservice.commons.json;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class BaseJson implements Serializable {

	private static final long serialVersionUID = 1L;

	private final static String DEFAULT_CORR_ID = "N/A";

	private String correlationID;

	/**
	 * Returns the Correlation ID of a request. <b>"N/A"</b> will be returned if the
	 * Corrleation ID is not set.
	 *
	 * @return
	 */
	public String getCorrelationID() {
		if (StringUtils.isBlank(correlationID)) {
			return DEFAULT_CORR_ID;
		} else {
			return correlationID;
		}
	}

	/**
	 * Set the Correlation ID of a request.
	 *
	 * @param correlationID
	 */
	public void setCorrelationID(String correlationID) {
		this.correlationID = StringUtils.trimToEmpty(correlationID);
	}

}
