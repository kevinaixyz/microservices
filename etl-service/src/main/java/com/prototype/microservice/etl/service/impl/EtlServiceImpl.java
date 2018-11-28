package com.prototype.microservice.etl.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prototype.microservice.commons.json.ResponseJson;
import com.prototype.microservice.etl.domain.RptJobStatus;
import com.prototype.microservice.etl.repository.RptJobRepository;
import com.prototype.microservice.etl.constant.AppConstant;
import com.prototype.microservice.etl.meta.CommonConfigInfo;
import com.prototype.microservice.etl.data.FileExtractor;
import com.prototype.microservice.etl.response.FileUploadResponseJson;
import com.prototype.microservice.etl.response.FileUploadStatusResponseJson;
import com.prototype.microservice.etl.pojo.EtlJobStatusPojo;
import com.prototype.microservice.etl.service.EtlService;
import com.prototype.microservice.etl.utils.BaseHelper;
import com.prototype.microservice.etl.utils.EtlMessageHelper;

@Service("rptService")
public class EtlServiceImpl implements EtlService {
	private BaseHelper baseHelper;
	private EtlMessageHelper msgHelper;
	private final FileExtractor fileExtractor;
	private final RptJobRepository rptJobRepository;

	@Autowired
	public EtlServiceImpl(FileExtractor fileExtractor, RptJobRepository rptJobRepository) {
		this.fileExtractor = fileExtractor;
		this.rptJobRepository = rptJobRepository;
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
			Map<String, String> sysColMap = new HashMap<>();
			sysColMap.put("FILE_NAME", fileName);
			List<CommonConfigInfo> configList = baseHelper.getConfigByFileNameInPattern(fileName);
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
			Date now= BaseHelper.getCurrentDateTime();
			String jobId = BaseHelper.genRptJobId();
			response.setMode("Async");
			jobStatus.setJobId(jobId);
			jobStatus.setStatus(AppConstant.ETL_JOB_STATUS_PROCSSING);
			jobStatus.setStartDatetime(now);
			byte[] decode = Base64.decodeBase64(fileStr);
			InputStream fileInputStream = new ByteArrayInputStream(decode);
			Map<String, String> sysColMap = new HashMap<>();
			sysColMap.put("FILE_NAME", fileName);
			List<CommonConfigInfo> configList = baseHelper.getConfigByFileNameInPattern(fileName);
			fileExtractor.readFileAsync(jobStatus, configList, fileInputStream, sysColMap);
			createJobStatus(jobStatus);
			response.setRespCode(ResponseJson.JSON_RESP_CODE_OK);
			response.setJobId(jobStatus.getJobId());
			response.setJobStatus(jobStatus.getStatus());
			//jobStatus=null;
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
	private void createJobStatus(RptJobStatus rptJobStatus){
		if(rptJobStatus!=null&&rptJobStatus.getJobId()!=null){
			RptJobStatus checkRptJobStatus = rptJobRepository.findOne(rptJobStatus.getJobId());
			if(checkRptJobStatus==null){
                rptJobRepository.save(rptJobStatus);
            }
		}
    }

	@Autowired
	public void setBaseHelper(BaseHelper baseHelper) {
		this.baseHelper = baseHelper;
	}

	@Autowired
	public void setMsgHelper(EtlMessageHelper msgHelper) {
		this.msgHelper = msgHelper;
	}
}
