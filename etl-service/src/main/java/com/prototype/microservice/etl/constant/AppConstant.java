package com.prototype.microservice.etl.constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;


public final class AppConstant {

	private AppConstant() {	}
	
	public final static String HK_TIME_ZONE = "UTC+8";
	public final static String ISO_TIMESTAMP_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	public final static String ISO_DATE_PATTERN = "yyyy-MM-dd";
	public final static String SQL_DATE_PATTERN = "YYYY-MM-DD";
	public final static String SQL_TIMESTAMP_PATTERN = "YYYY-MM-DD\"T\"HH24:MI:SS.FF3TZHTZM";
	public final static String BigDecimal_2Decimal_PATTERN = "BigDecimal_2Decimal";
	public final static String BigDecimal_3Decimal_PATTERN = "BigDecimal_3Decimal";
	public final static String BigDecimal_4Decimal_PATTERN = "BigDecimal_4Decimal";
	public final static String RPT_LOGGER_NAME = "RPT";
	public final static String RPT_LOGGER_APPENDER_NAME = "RPT_DAILY_LOGFILE";
	public final static String RPT_JOB_ID_PATTERN="yyyyMMddHHmmssSSS";
	public final static String RPT_JOB_STATUS_PROCSSING = "processing";
	public final static String RPT_JOB_STATUS_SUCCESS = "success";
	public final static String RPT_JOB_STATUS_FAILED = "failed";
	
	@Value("${file.path}")
	private static String path;
	
	
	public static  String PROP_FILE_LOCATION_CMDS = "";
	public static  String TEMP_FILE_LOCATION= "";
	static
	   {
		Properties propTemp = new Properties();
		 try {
	        	
				
				InputStream fis = null;
				
				
				//System.err.print("==========datafile============="+path+"=="+System.getProperty("cdms.home")+"======");
				if(path!=null&&!path.equals(""))
				{
					File properfiesFile = new File(path);
					fis =new FileInputStream(properfiesFile);
					System.err.print("==========read argument=============");
				}
				else
				{
				fis = AppConstant.class.getClassLoader().getResourceAsStream("cdms-init.properties");
				}
				
				propTemp.load(fis);
				
				
				
			 } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 PROP_FILE_LOCATION_CMDS =propTemp.getProperty("cdms.home","/var/cdms/");
		 TEMP_FILE_LOCATION = propTemp.getProperty("cdms.temp","/var/cdms/");
	   }
}
