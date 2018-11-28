package com.prototype.microservice.etl.data;

import java.io.InputStream;
import java.util.Map;

import org.springframework.stereotype.Component;


@Component
public class WebRequestProcessor extends EtlProcessorTemplate {
	private InputStream in;
	private Map<String, String> sysColValMap;
	

	public InputStream getIn() {
		return in;
	}

	public void setIn(InputStream in) {
		this.in = in;
	}

	public Map<String, String> getSysColValMap() {
		return sysColValMap;
	}

	public void setSysColValMap(Map<String, String> sysColValMap) {
		this.sysColValMap = sysColValMap;
	}

	//For UI request
	@Override
	public int process() throws Exception{
		checkDataExistByFileName(sysColValMap);
		etlFileParser.setIn(in);
		etlFileParser.setSysColValMap(sysColValMap);
		return  etlFileParser.parseFile();
	}
}
