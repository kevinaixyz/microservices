package com.prototype.microservice.etl.service;

import com.prototype.microservice.commons.json.ResponseJson;
import com.prototype.microservice.etl.data.CommonConfigInfo;

public interface EtlService {
	public void setConfigInfo(CommonConfigInfo configInfo);
	public CommonConfigInfo getConfigInfo();
	
	public ResponseJson readFileByBase64Async(String fileStr, String fileType);
	public ResponseJson readFileByBase64Sync(String fileStr, String fileType);

	public ResponseJson checkRptJobStatus(String jobId);
}
