package com.prototype.microservice.etl.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prototype.microservice.commons.json.ResponseJson;
import com.prototype.microservice.etl.cdms.domain.RptJobStatus;
import com.prototype.microservice.etl.cdms.repository.impl.RptJobRepository;
import com.prototype.microservice.etl.constant.AppConstant;
import com.prototype.microservice.etl.data.CommonConfigInfo;
import com.prototype.microservice.etl.data.FileExtractor;
import com.prototype.microservice.etl.json.FileUploadResponseJson;
import com.prototype.microservice.etl.json.FileUploadStatusResponseJson;
import com.prototype.microservice.etl.pojo.EtlJobStatusPojo;
import com.prototype.microservice.etl.service.EtlService;
import com.prototype.microservice.etl.utils.EtlHelper;
import com.prototype.microservice.etl.utils.EtlMessageHelper;

@Service("rptService")
public class EtlServiceImpl implements EtlService {
	private Logger rptLogger;
	private CommonConfigInfo configInfo;
	@Autowired
	private EtlHelper rptHelper;
	@Autowired
	FileExtractor fileExtractor;
	@Autowired
	private RptJobRepository rptJobRepository;
	@Autowired
	private EtlMessageHelper msgHelper;
	@Override
	public void setConfigInfo(CommonConfigInfo configInfo) {
		this.configInfo=configInfo;
		rptLogger = LoggerFactory.getLogger(EtlHelper.getLoggerName(configInfo.getFileNamePattern(), EtlHelper.getCurrentDateStr()));
	}
	@Override
	public CommonConfigInfo getConfigInfo() {
		return configInfo;
	}
	@Override
	public ResponseJson readFileByBase64Sync(String fileStr, String fileName) {
		FileUploadResponseJson response = new FileUploadResponseJson();
		try{
			response.setMode("Sync");
			response.setJobId(null);
			response.setJobStatus(null);

			byte[] decode = Base64.decodeBase64(fileStr);
			InputStream fileInputStream = new ByteArrayInputStream(decode);
			Map<String, String> sysColMap = new HashMap<String, String>();
			sysColMap.put("FILE_NAME", fileName);
			List<CommonConfigInfo> configList = rptHelper.getConfigByFileNameinPattern(fileName);
			List<Integer> recNumList = fileExtractor.readFileSync(configList, fileInputStream, sysColMap);
			response.setProcessedRecNumList(recNumList);
			response.setRespCode(ResponseJson.JSON_RESP_CODE_OK);
			response.setRespMsg(msgHelper.getMessage("RPT-INF-002", new Object[]{recNumList.toString()}));
		}catch(Exception e){
			response.setRespCode(ResponseJson.JSON_RESP_CODE_ERROR);
			response.setRespMsg(e.getMessage());
			e.printStackTrace();
		}
		return response;
	}
	
	@Override
	public ResponseJson readFileByBase64Async(String fileStr, String fileName) {
		FileUploadResponseJson response = new FileUploadResponseJson();
		try{
			RptJobStatus jobStatus = new RptJobStatus();
			//Create RPT Job Status
			Date now= EtlHelper.getCurrentDateTime();
			String jobId = EtlHelper.genRptJobId();
			response.setMode("Async");
			jobStatus.setJobId(jobId);
			jobStatus.setStatus(AppConstant.RPT_JOB_STATUS_PROCSSING);
			jobStatus.setStartDatetime(now);
			byte[] decode = Base64.decodeBase64(fileStr);
			InputStream fileInputStream = new ByteArrayInputStream(decode);
			Map<String, String> sysColMap = new HashMap<String, String>();
			sysColMap.put("FILE_NAME", fileName);
			List<CommonConfigInfo> configList = rptHelper.getConfigByFileNameinPattern(fileName);
			fileExtractor.readFileAsync(jobStatus, configList, fileInputStream, sysColMap);
			createJobStatus(jobStatus);
			response.setRespCode(ResponseJson.JSON_RESP_CODE_OK);
			response.setJobId(jobStatus.getJobId());
			response.setJobStatus(jobStatus.getStatus());
			jobStatus=null;
			//response.setRespMsg(MessageFormat.format("Successfully uploaded {0} records", new Object[]{futureRes.get()}));
		}catch(Exception e){
			response.setRespCode(ResponseJson.JSON_RESP_CODE_ERROR);
			response.setRespMsg(e.getMessage());
			e.printStackTrace();
		}
		return response;
	}
	
	@Override
	public ResponseJson checkRptJobStatus(String jobId){
		RptJobStatus jobStatus = rptJobRepository.findOne(jobId);
		FileUploadStatusResponseJson response = new FileUploadStatusResponseJson();
		try{
			if(jobStatus!=null){
				EtlJobStatusPojo pojo = new EtlJobStatusPojo();
				pojo.setRptJobId(jobId);
				pojo.setStatus(jobStatus.getStatus());
				pojo.setStartTime(jobStatus.getStartDatetime().toString());
				if(jobStatus.getEndDatetime()!=null){
					pojo.setEndTime(jobStatus.getEndDatetime().toString());					
				}
				if(jobStatus.getResultList()!=null){
					pojo.setResultList(jobStatus.getResultList());					
				}
				response.setStatus(pojo);
				response.setRespCode(ResponseJson.JSON_RESP_CODE_OK);
			}
		}catch(Exception e){
			response.setRespCode(ResponseJson.JSON_RESP_CODE_ERROR);
		}
		return response;
	}
	public RptJobStatus createJobStatus(RptJobStatus rptJobStatus){
		if(rptJobStatus!=null&&rptJobStatus.getJobId()!=null){
			RptJobStatus checkRptJobStatus = rptJobRepository.findOne(rptJobStatus.getJobId());
			if(checkRptJobStatus==null){
				return rptJobRepository.save(rptJobStatus);				
			}else{
				return checkRptJobStatus;
			}
		}
		return null;
	}
}
