package com.prototype.microservice.etl.data;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;


@Component
public class MsRequestProcessor extends RptActionProcessor{
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

	public RptFileParser getRptFileParser() {
		return rptFileParser;
	}

	public void setRptFileParser(RptFileParser rptFileParser) {
		this.rptFileParser = rptFileParser;
	}

	//For UI request
	@Override
	public int process() throws Exception{
		checkDataExistByFileName(sysColValMap);
		rptFileParser.setIn(in);
		rptFileParser.setSysColValMap(sysColValMap);
		return  rptFileParser.parseFile();
	}
	
	public void checkDataExistByFileName(Map<String, String> sysColValMap){
		if(configInfo==null||configInfo.getSystemColumns()==null){
			return;
		}
		if(sysColValMap!=null){
			List<ColumnMetaInfo> sysColumns = configInfo.getSystemColumns();
			List<String> checkVals = new ArrayList<String>();
			sysColumns.forEach(s->{
				if(sysColValMap.containsKey(s.getTableColName())){
					checkVals.add(sysColValMap.get(s.getTableColName()));
				}
			});
			boolean isExist = isDataExist(configInfo.getDbTableName(),sysColumns, checkVals);
			if(isExist){
				deleteDataByColumns(configInfo.getDbTableName(), sysColumns, checkVals);
			}
		}
	}
}
