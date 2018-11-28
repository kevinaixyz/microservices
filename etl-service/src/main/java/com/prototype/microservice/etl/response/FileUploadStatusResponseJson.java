package com.prototype.microservice.etl.response;

import com.prototype.microservice.commons.json.SimpleResponseJson;
import com.prototype.microservice.etl.pojo.EtlJobStatusPojo;

public class FileUploadStatusResponseJson extends SimpleResponseJson{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private EtlJobStatusPojo status;

	public EtlJobStatusPojo getStatus() {
		return status;
	}

	public void setStatus(EtlJobStatusPojo status) {
		this.status = status;
	}
	
}
