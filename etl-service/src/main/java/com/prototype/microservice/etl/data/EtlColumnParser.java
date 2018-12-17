package com.prototype.microservice.etl.data;


import java.math.BigDecimal;
import java.util.List;

import com.prototype.microservice.etl.meta.ColumnMetaInfo;
import org.apache.commons.lang3.StringUtils;

import com.prototype.microservice.etl.constant.AppConstant;
import com.prototype.microservice.etl.utils.BaseHelper;

public class EtlColumnParser {
	public static String parseColumnValueForNative(String raw, ColumnMetaInfo columnInfo, List<String> nullValFilter){
		String value=null;
		if(StringUtils.isBlank(raw)||columnInfo==null){
			return null;
		}
		if(nullValFilter!=null&&nullValFilter.size()>0){
			for(String nullVal:nullValFilter){
				if(nullVal.toLowerCase().equals(raw.toLowerCase())){
					return null;
				}
			}
		}
		String format = columnInfo.getFormat();
		if(StringUtils.isBlank(format)){
			value = raw;
		}
		switch (StringUtils.trim(columnInfo.getType()).toLowerCase()) {
			case ColumnMetaInfo.DB_TYPE_TIMESTAMP:
				if (StringUtils.isNotBlank(format)) {
					value = BaseHelper.formatLocalDateTime(raw, format);
				}
				value = BaseHelper.timestampFieldToNativeSql(value, AppConstant.SQL_TIMESTAMP_PATTERN);
				return value;
			case ColumnMetaInfo.DB_TYPE_DATE:
				if (StringUtils.isNotBlank(format)) {
					value = BaseHelper.formatLocalDate(raw, format);
				}
				value = BaseHelper.dateFieldToNativeSql(value, AppConstant.SQL_DATE_PATTERN);
				return value;
			case ColumnMetaInfo.DB_TYPE_CHAR:
				value = escapeSpecialChar(raw.trim());
				value = "'" + value + "'";
				return value;
			case ColumnMetaInfo.DB_TYPE_NUMBER:
				BigDecimal b = BaseHelper.stringToBigDecimal(raw);
				if (b != null) {
					value = b.toString();
				} else {
					value = null;
				}
				return value;
		}
		/*else if(StringUtils.trim(columnInfo.getType()).toLowerCase().equals(AppConstant.BigDecimal_2Decimal_PATTERN)){
			value = escapeSpecialChar(raw.trim());
			value = String.valueOf(new BigDecimal(value).divide(new BigDecimal("100")).doubleValue());
			value = ""+value+"";
			return value;
		}
		else if(StringUtils.trim(columnInfo.getType()).toLowerCase().equals(AppConstant.BigDecimal_3Decimal_PATTERN)){
			value = escapeSpecialChar(raw.trim());
			value = String.valueOf(new BigDecimal(value).divide(new BigDecimal("1000")).doubleValue());
			value = ""+value+"";
			return value;
		}
		else if(StringUtils.trim(columnInfo.getType()).toLowerCase().equals(AppConstant.BigDecimal_4Decimal_PATTERN)){
			value = escapeSpecialChar(raw.trim());
			value = String.valueOf(new BigDecimal(value).divide(new BigDecimal("10000")).doubleValue());
			value = ""+value+"";
			return value;
		}*/
		return raw.trim();
	}
	public static Object parseColumnForParam(String raw, ColumnMetaInfo columnInfo){
		Object value=null;
		if(StringUtils.isBlank(raw)||columnInfo==null){
			return null;
		}
		String format = columnInfo.getFormat();
		if(StringUtils.isBlank(format)){
			value = raw;
		}
		switch (StringUtils.trim(columnInfo.getType()).toLowerCase()) {
			case ColumnMetaInfo.DB_TYPE_TIMESTAMP: {
				if (StringUtils.isNotBlank(format)) {
					value = BaseHelper.parseDatetime(raw, format);
				}else{
					value = BaseHelper.parseDatetime(raw, AppConstant.ISO_TIMESTAMP_PATTERN);
				}
				return value;
			}
			case ColumnMetaInfo.DB_TYPE_DATE: {
				if (StringUtils.isNotBlank(format)) {
					value = BaseHelper.parseDate(raw, format);
				}else{
					value = BaseHelper.dateFieldToNativeSql(raw, AppConstant.ISO_DATE_PATTERN);
				}
				return value;
			}
			case ColumnMetaInfo.DB_TYPE_NUMBER: {
				if (StringUtils.isNotBlank(format)) {
					if (format.toLowerCase().equals(ColumnMetaInfo.JAVA_TYPE_BIGDECIMAL)) {
						value = new BigDecimal(raw.trim());
					} else if (format.toLowerCase().equals(ColumnMetaInfo.JAVA_TYPE_INTEGER)) {
						value = Integer.parseInt(raw.trim());
					} else if (format.toLowerCase().equals(ColumnMetaInfo.JAVA_TYPE_DOUBLE)) {
						value = Double.parseDouble(raw.trim());
					} else if (StringUtils.trim(columnInfo.getType()).toLowerCase().equals(AppConstant.BigDecimal_2Decimal_PATTERN)) {
						value = new BigDecimal(escapeSpecialChar(raw.trim())).divide(new BigDecimal("100")).doubleValue();
					} else if (StringUtils.trim(columnInfo.getType()).toLowerCase().equals(AppConstant.BigDecimal_3Decimal_PATTERN)) {
						value = new BigDecimal(escapeSpecialChar(raw.trim())).divide(new BigDecimal("1000")).doubleValue();
					} else if (StringUtils.trim(columnInfo.getType()).toLowerCase().equals(AppConstant.BigDecimal_4Decimal_PATTERN)) {
						value = new BigDecimal(escapeSpecialChar(raw.trim())).divide(new BigDecimal("10000")).doubleValue();
					}
				}
				return value;
			}
		}
		return raw.trim();
	}
	
	public static String escapeSpecialChar(String src){
		//String[] specChar = new String[]{"'","&"};
		if(src.contains("'")){
			src = src.replace("'", "''");
		}
		if(src.contains("&")){
			src = src.replace("&", "'||'&'||'");
		}
		return src;
	}
	
}
