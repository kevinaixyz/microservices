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
	public final static String ETL_LOGGER_NAME = "RPT";
	public final static String ETL_LOGGER_APPENDER_NAME = "RPT_DAILY_LOGFILE";
	public final static String ETL_JOB_ID_PATTERN ="yyyyMMddHHmmssSSS";
	public final static String ETL_JOB_STATUS_PROCSSING = "processing";
	public final static String ETL_JOB_STATUS_SUCCESS = "success";
	public final static String ETL_JOB_STATUS_FAILED = "failed";

}
