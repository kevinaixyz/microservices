package com.prototype.microservice.etl.data;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import com.prototype.microservice.etl.utils.EtlHelper;
@Component
public class BatchJobProcessor extends RptActionProcessor{
	public final static String SYS_COL_FILE_DATE="FILE_DATE";
	public final static String SYS_COL_CRE_DATE="CRE_DATE";
	protected File file;
	private Map<String, String> sysColValMap;
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
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
	//For batch job
	public int process(){
		if(file!=null&&file.exists()){
			ByteArrayInputStream in = null;
			try {
				rptLogger.info("Parsing file ============>"+file.getCanonicalPath());
				checkDataExistByFileDate();
				rptFileParser.setSysColValMap(sysColValMap);
				in = new ByteArrayInputStream(FileUtils.readFileToByteArray(file));
				rptFileParser.setIn(in);
				return rptFileParser.parseFile();
			} catch (FileNotFoundException e) {
				rptLogger.error(e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				rptLogger.error(e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				rptLogger.error(e.getMessage());
				e.printStackTrace();
			} finally{
				if(sysColValMap!=null){
					sysColValMap.clear();
					sysColValMap = null;
				}
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return 0;
	}
	
	public void checkDataExistByFileDate(){
		if(configInfo==null||configInfo.getSystemColumns()==null){
			return;
		}
		List<ColumnMetaInfo> sysColumns = configInfo.getSystemColumns();
		List<String> checkVals = new ArrayList<String>();
		try{
			sysColumns.forEach(s -> {
				if (SYS_COL_FILE_DATE.equalsIgnoreCase(s.getTableColName())) {
					if(file!=null){
						String fileDateStr = EtlHelper.getFileDateStr(file, configInfo.getFileDateFormat());
						checkVals.add(fileDateStr);						
					}
				}
			});
			boolean isExist = isDataExist(configInfo.getDbTableName(), sysColumns, checkVals);
			if (isExist) {
				deleteDataByColumns(configInfo.getDbTableName(), sysColumns, checkVals);
			}
		}catch(Exception e){
			rptLogger.error(e.getMessage());
		}
	}
}
