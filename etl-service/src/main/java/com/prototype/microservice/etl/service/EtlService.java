package com.prototype.microservice.etl.service;

import com.prototype.microservice.commons.json.ResponseJson;

public interface EtlService {
	ResponseJson readFileByBase64Async(String fileStr, String fileType);
	ResponseJson readFileByBase64Sync(String fileStr, String fileType);
	ResponseJson checkRptJobStatus(String jobId);
}
