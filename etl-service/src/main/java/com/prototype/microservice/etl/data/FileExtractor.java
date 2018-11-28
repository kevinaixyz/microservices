package com.prototype.microservice.etl.data;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.prototype.microservice.etl.meta.CommonConfigInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.prototype.microservice.etl.domain.RptJobStatus;
import com.prototype.microservice.etl.repository.RptJobRepository;
import com.prototype.microservice.etl.constant.AppConstant;
import com.prototype.microservice.etl.utils.BaseHelper;

@Component
public class FileExtractor {
	@Autowired
	private RptJobRepository rptJobRepository;
	@Autowired
    WebRequestProcessor webRequestProcessor;
	@Async
	public Future<RptJobStatus> readFileAsync(RptJobStatus jobStatus, List<CommonConfigInfo> configList, InputStream in, Map<String, String> sysColMap){
		List<Integer> recNumList = null;
		try{
			recNumList = processFile(configList, in, sysColMap);
			rptJobRepository.update(jobStatus);
			jobStatus.setResultList(recNumList.toString());
			jobStatus.setStatus(AppConstant.ETL_JOB_STATUS_SUCCESS);
			jobStatus.setEndDatetime(BaseHelper.getCurrentDateTime());
		}catch(Exception e){
			jobStatus.setResultList(recNumList.toString());
			jobStatus.setStatus(AppConstant.ETL_JOB_STATUS_FAILED);
			jobStatus.setEndDatetime(BaseHelper.getCurrentDateTime());
		}
		return new AsyncResult<RptJobStatus>(jobStatus);
	}
	public List<Integer> readFileSync(List<CommonConfigInfo> configList, InputStream in, Map<String, String> sysColMap) throws Exception{
		return processFile(configList, in, sysColMap);
	}
	public List<Integer> processFile(List<CommonConfigInfo> configList, InputStream in, Map<String, String> sysColMap) throws Exception {
		List<Integer> recNumList = new ArrayList<Integer>();
		if(configList!=null&&configList.size()>0){
			for(CommonConfigInfo c:configList){
				webRequestProcessor.setConfigInfo(c);
				webRequestProcessor.setIn(in);
				webRequestProcessor.setSysColValMap(sysColMap);
				int recNum = webRequestProcessor.process();
				recNumList.add(recNum);
			}
		}
		return recNumList;
	}
}
