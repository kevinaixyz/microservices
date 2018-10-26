package com.prototype.microservice.etl.data;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.prototype.microservice.etl.cdms.domain.RptJobStatus;
import com.prototype.microservice.etl.cdms.repository.impl.RptJobRepository;
import com.prototype.microservice.etl.constant.AppConstant;
import com.prototype.microservice.etl.utils.EtlHelper;

@Component
public class FileExtractor {
	@Autowired
	private RptJobRepository rptJobRepository;
	@Autowired
	MsRequestProcessor msRequestProcessor;	
	@Async
	public Future<RptJobStatus> readFileAsync(RptJobStatus jobStatus, List<CommonConfigInfo> configList, InputStream in, Map<String, String> sysColMap){
		List<Integer> recNumList = new ArrayList<Integer>();
		try{
			if(configList!=null&&configList.size()>0){
				for(CommonConfigInfo c:configList){
					msRequestProcessor.setConfigInfo(c);
					msRequestProcessor.setIn(in);
					msRequestProcessor.setSysColValMap(sysColMap);
					int recNum = msRequestProcessor.process();
					recNumList.add(recNum);
				}
				rptJobRepository.update(jobStatus);
			}
			jobStatus.setResultList(recNumList.toString());
			jobStatus.setStatus(AppConstant.RPT_JOB_STATUS_SUCCESS);
			jobStatus.setEndDatetime(EtlHelper.getCurrentDateTime());
		}catch(Exception e){
			jobStatus.setResultList(recNumList.toString());
			jobStatus.setStatus(AppConstant.RPT_JOB_STATUS_FAILED);
			jobStatus.setEndDatetime(EtlHelper.getCurrentDateTime());
		}
		return new AsyncResult<RptJobStatus>(jobStatus);
	}
	public List<Integer> readFileSync(List<CommonConfigInfo> configList, InputStream in, Map<String, String> sysColMap) throws Exception{
		List<Integer> recNumList = new ArrayList<Integer>();
		if(configList!=null&&configList.size()>0){
			for(CommonConfigInfo c:configList){
				msRequestProcessor.setConfigInfo(c);
				msRequestProcessor.setIn(in);
				msRequestProcessor.setSysColValMap(sysColMap);
				int recNum = msRequestProcessor.process();
				recNumList.add(recNum);
			}
		}
		return recNumList;
	}
}
