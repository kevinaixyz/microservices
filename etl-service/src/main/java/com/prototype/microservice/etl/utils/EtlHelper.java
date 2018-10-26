package com.prototype.microservice.etl.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.prototype.microservice.etl.constant.AppConstant;
import com.prototype.microservice.etl.data.ColumnMetaInfo;
import com.prototype.microservice.etl.data.CommonConfigInfo;
import com.prototype.microservice.etl.data.RptOverallConfig;

@Component
@DependsOn("overallConfig")
public class EtlHelper {
	private static final String REGEX_NUMBER = "^[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?$";
	@Autowired
	private RptOverallConfig overallConfig;
	
	public RptOverallConfig getOverallConfig(){
		return overallConfig;
	}
	public String getTableName(CommonConfigInfo configInfo){
		return configInfo.getDbTableName();
	}
	public String getFileDateFormat(CommonConfigInfo configInfo){
		return configInfo.getFileDateFormat();
	}
	public String getFilePath(CommonConfigInfo configInfo){
		return configInfo.getFilePath();
	}
	public String getFileNamePattern(CommonConfigInfo configInfo){
		return configInfo.getFileNamePattern();
	}
	public String getLogPath(CommonConfigInfo configInfo){
		return configInfo.getLogFilePath();
	}
	public String getLogNamePattern(CommonConfigInfo configInfo){
		return configInfo.getLogFileNamePattern();
	}
	public String getColDelimiter(CommonConfigInfo configInfo){
		return configInfo.getColDelimiter();
	}
	public static List<File> getFiles(CommonConfigInfo configInfo, String dateStr) throws Exception{
		List<File> files = new ArrayList<File>();
		if (configInfo.getFilePath() != null && configInfo.getFileNamePattern() != null) {
			String fileName = configInfo.getFileNamePattern().replace("{fileDateFormat}", dateStr);

			File dir = new File(configInfo.getFilePath());
			if (dir.exists() && dir.isDirectory()) {
				Path startPath = dir.toPath();
				Files.find(startPath, Integer.MAX_VALUE, (filePath, fileAttr) -> {
					if (configInfo.getFileType() != null
							&& CommonConfigInfo.FILE_TYPE_EXCEL.equalsIgnoreCase(configInfo.getFileType())) {
						return fileAttr.isRegularFile() && (filePath.getFileName().toString().equalsIgnoreCase(fileName + ".xls")
								|| filePath.getFileName().toString().equalsIgnoreCase(fileName + ".xlsx"));
					} else {
						return fileAttr.isRegularFile() && (filePath.getFileName().toString().equals(fileName)
								|| filePath.getFileName().toString().equals(fileName));
					}

				}).forEach(p->{files.add(p.toFile());});
				//file = target.toFile();
			}
			if (files.size()==0) {
				throw new FileNotFoundException(MessageFormat.format("Cannot find file {0} under the path {1}",
						new Object[] { fileName, configInfo.getFilePath() }));
			}
		}
		return files;
	}
	public static String convertDateFormatToRegex(final String dateFormat){
		String fileDateRegex = dateFormat;
		fileDateRegex = fileDateRegex.replace("d", "\\d");
		fileDateRegex = fileDateRegex.replace("D", "\\d");
		fileDateRegex = fileDateRegex.replace("m", "\\d");
		fileDateRegex = fileDateRegex.replace("M", "\\d");
		fileDateRegex = fileDateRegex.replace("y", "\\d");
		fileDateRegex = fileDateRegex.replace("Y", "\\d");
		return fileDateRegex;
	}
	public static List<File> getFiles(CommonConfigInfo configInfo) throws Exception{
		List<File> files = new ArrayList<File>();
		if (StringUtils.isNotBlank(configInfo.getFilePath()) && StringUtils.isNotBlank(configInfo.getFileNamePattern()) && StringUtils.isNotBlank(configInfo.getFileDateFormat())) {
			String fileDateRegex = convertDateFormatToRegex(configInfo.getFileDateFormat());
			String fileName = configInfo.getFileNamePattern().replace("{fileDateFormat}", fileDateRegex);
			
			File dir = new File(configInfo.getFilePath());
			if (dir.exists() && dir.isDirectory()) {
				Path startPath = dir.toPath();
				Files.find(startPath, Integer.MAX_VALUE, (filePath, fileAttr) -> {
					if (configInfo.getFileType() != null
							&& CommonConfigInfo.FILE_TYPE_EXCEL.equalsIgnoreCase(configInfo.getFileType())) {
						return fileAttr.isRegularFile() && (filePath.getFileName().toString().matches(fileName + ".xls")
								|| filePath.getFileName().toString().matches(fileName + ".xlsx"));
					} else {
						return fileAttr.isRegularFile() && (filePath.getFileName().toString().matches(fileName)
								|| filePath.getFileName().toString().matches(fileName));
					}

				}).forEach(p->{files.add(p.toFile());});
				//file = target.toFile();
			}
			if (files.size()==0) {
				throw new FileNotFoundException(MessageFormat.format("Cannot find file {0} under the path {1}",
						new Object[] { fileName, configInfo.getFilePath() }));
			}
		}
		return files;
	}
	public static String getFileDateStr(File file, final String fileDateFormat){
		String fileDateValue = null;
		String dateRegex = convertDateFormatToRegex(fileDateFormat);
		
		Matcher m = Pattern.compile("("+dateRegex+")").matcher(file.getName());
		if (m.find()) {
			fileDateValue = m.group(1);
		}
		return fileDateValue;
	}
	public List<CommonConfigInfo> getExcelConfigList(){
		List<CommonConfigInfo> excelConfigList = new ArrayList<CommonConfigInfo>();
		if(overallConfig!=null&&overallConfig.getConfigList()!=null){
			overallConfig.getConfigList().forEach(c->{
				if(c.getFileType().equalsIgnoreCase(CommonConfigInfo.FILE_TYPE_EXCEL)){
					excelConfigList.add(c);
				}
			});
		}
		return excelConfigList;
	}
	public List<CommonConfigInfo> getCsvConfigList(){
		List<CommonConfigInfo> csvConfigList = new ArrayList<CommonConfigInfo>();
		if(overallConfig!=null&&overallConfig.getConfigList()!=null){
			overallConfig.getConfigList().forEach(c->{
				if(c.getFileType().equalsIgnoreCase(CommonConfigInfo.FILE_TYPE_CSV)){
					csvConfigList.add(c);
				}
			});
		}
		return csvConfigList;
	}
	public List<CommonConfigInfo> getTxtConfigList(){
		List<CommonConfigInfo> txtConfigList = new ArrayList<CommonConfigInfo>();
		if(overallConfig!=null&&overallConfig.getConfigList()!=null){
			overallConfig.getConfigList().forEach(c->{
				if(c.getFileType().equalsIgnoreCase(CommonConfigInfo.FILE_TYPE_TXT)){
					txtConfigList.add(c);
				}
			});
		}
		return txtConfigList;
	}
	public List<CommonConfigInfo> getConfigByFileName(String fileName){
		List<CommonConfigInfo> configList = new ArrayList<CommonConfigInfo>();
		if(overallConfig!=null&&overallConfig.getConfigList()!=null){
			overallConfig.getConfigList().forEach(c->{
				String fileNamePtn = c.getFileNamePattern();
				String dateFormat = c.getFileDateFormat();
				dateFormat = dateFormat.replaceAll("d", "\\\\d");
				dateFormat = dateFormat.replaceAll("D", "\\\\d");
				dateFormat = dateFormat.replaceAll("y", "\\\\d");
				dateFormat = dateFormat.replaceAll("Y", "\\\\d");
				dateFormat = dateFormat.replaceAll("m", "\\\\d");
				dateFormat = dateFormat.replaceAll("M", "\\\\d");
				
				String newPattern = fileNamePtn.replace("{fileDateFormat}", "("+dateFormat+")");
				String fileNameWithoutExt = fileName.substring(0, fileName.lastIndexOf("."));
				boolean isMatch = fileNameWithoutExt.matches(newPattern);
				if(isMatch){
					configList.add(c);
				}
			});
		}
		return configList;
	}
	
	public List<CommonConfigInfo> getConfigByFileNameinPattern(String fileName){
		List<CommonConfigInfo> configList = new ArrayList<CommonConfigInfo>();
		if(overallConfig!=null&&overallConfig.getConfigList()!=null){
			overallConfig.getConfigList().forEach(c->{
				String fileNamePtn = c.getFileNamePattern();
				String dateFormat = c.getFileDateFormat();
				dateFormat = dateFormat.replaceAll("d", "\\\\d");
				dateFormat = dateFormat.replaceAll("D", "\\\\d");
				dateFormat = dateFormat.replaceAll("y", "\\\\d");
				dateFormat = dateFormat.replaceAll("Y", "\\\\d");
				dateFormat = dateFormat.replaceAll("m", "\\\\d");
				dateFormat = dateFormat.replaceAll("M", "\\\\d");
				
				String newPattern = fileNamePtn.replace("{fileDateFormat}", "");
				//String fileNameWithoutExt = fileName.substring(0, fileName.lastIndexOf("."));
				String extension = FilenameUtils.getExtension(fileName);
				String pattern = newPattern+"[0-9]{0,8}."+extension;
				
				 Pattern r = Pattern.compile(pattern);
				 Matcher m = r.matcher(fileName);
				
				//boolean isMatch = fileNameWithoutExt.matches(newPattern);
				
				if(m.find())
				{
					configList.add(c);
				}
				
				//if(isMatch){
				//	configList.add(c);
				//}
			});
		}
		return configList;
	}
	
	public List<ColumnMetaInfo> getColumnsMetaInfo(CommonConfigInfo configInfo) {
		return configInfo.getColumns();
	}
	public List<ColumnMetaInfo> getSystemColumnsMetaInfo(CommonConfigInfo configInfo){
		return configInfo.getSystemColumns();
	}
	public static String getLoggerName(String fileNamePattern, String dateStr){
		String loggerName = fileNamePattern.split("\\.")[0];
		if(loggerName.contains("{fileDateFormat}")){
			loggerName=loggerName.replace("{fileDateFormat}", dateStr);
		}
		return loggerName;
	}
	public static LocalDate parseDate(String src, String srcFormat){
		LocalDate localDate = LocalDate.parse(src, DateTimeFormatter.ofPattern(srcFormat));
		return localDate;
	}
	public static LocalDateTime parseDatetime(String src, String srcFormat){
		LocalDateTime localDateTime = LocalDateTime.parse(src, DateTimeFormatter.ofPattern(srcFormat));
		return localDateTime;
	}
	public static String formatDate(LocalDate localDate, String format){
		String dateStr = null;
		if(localDate!=null&&StringUtils.isNotBlank(format)){
			dateStr = localDate.format(DateTimeFormatter.ofPattern(format));
		}
		return dateStr;
	}
	public static String formatLocalDate(LocalDate localDate){
		String dateStr = null;
		ZonedDateTime datetime = ZonedDateTime.of(localDate.atStartOfDay(), ZoneId.of(AppConstant.HK_TIME_ZONE));
		dateStr = datetime.format(DateTimeFormatter.ofPattern(AppConstant.ISO_DATE_PATTERN));
		return dateStr;
	}
	public static String formatLocalDate(String src, String srcFormat){
		LocalDate localDate = parseDate(src, srcFormat);
		return formatLocalDate(localDate);
	}
	public static String formatLocalDateTime(LocalDateTime localDateTime){
		String dateStr = null;
		ZonedDateTime datetime = ZonedDateTime.of(localDateTime, ZoneId.of(AppConstant.HK_TIME_ZONE));
		dateStr = datetime.format(DateTimeFormatter.ofPattern(AppConstant.ISO_TIMESTAMP_PATTERN));
		return dateStr;
	}
	public static String formatLocalDateTime(String src, String srcFormat){
		LocalDateTime localDateTime = parseDatetime(src, srcFormat);
		return formatLocalDateTime(localDateTime);
	}
	public static String dateFieldToNativeSql(String value, String sqlDateFormat){
		StringBuilder dateSql = new StringBuilder("TO_DATE('");
		dateSql.append(value);
		dateSql.append("','");
		dateSql.append(sqlDateFormat);
		dateSql.append("')");
		return dateSql.toString();
	}
	public static String timestampFieldToNativeSql(String value, String sqlTimestampFormat){
		StringBuilder dateSql = new StringBuilder("TO_TIMESTAMP_TZ('");
		dateSql.append(value);
		dateSql.append("','");
		dateSql.append(sqlTimestampFormat);
		dateSql.append("')");
		return dateSql.toString();
	}
	public static Date getCurrentDate(){
		LocalDate localDate = LocalDate.now();
		Date date = Date.from(localDate.atStartOfDay().atZone(ZoneId.of(AppConstant.HK_TIME_ZONE)).toInstant());
		return date;
	}
	public static Date getCurrentDateTime(){
		LocalDateTime localDateTime = LocalDateTime.now();
		Instant instant = localDateTime.atZone(ZoneId.of(AppConstant.HK_TIME_ZONE)).toInstant();
		return Date.from(instant);
	}
	public static String getCurrentDateStr(){
		String dateStr = null;
		SimpleDateFormat isoDf =  new SimpleDateFormat(AppConstant.ISO_DATE_PATTERN);
		Date now = getCurrentDate();
		dateStr = isoDf.format(now);
		return dateStr;
		
	}
	public static String getCurrentDatetimeStr(){
		SimpleDateFormat isoDf =  new SimpleDateFormat(AppConstant.ISO_TIMESTAMP_PATTERN);
		Date date = getCurrentDateTime();
		return isoDf.format(date);
	}
	public String formatRawDate(Date date, String dateFormat){
		if(dateFormat==null){
			return null;
		}
		SimpleDateFormat df = new SimpleDateFormat(dateFormat);
		return df.format(date);
	}
	public static boolean isNumber(String str){
		if(str.matches(REGEX_NUMBER)){
			return true;
		}
		return false;
	}
	public static BigDecimal stringToBigDecimal(String str){
		if(StringUtils.isNotBlank(str)&&isNumber(str.trim())){
			return new BigDecimal(str.trim());
		}
		return null;
	}
	public static String[] getKeyValueSplitByEq(String str){
		if(StringUtils.isNotBlank(str)){
			String[] pair = str.split("=");
			if(pair.length==0){
				throw new RuntimeException("Cannot split by '='. ");
			}else if(pair.length==1){
				String field = pair[0];
				return new String[]{field, null};
			}else if(pair.length==2){
				return pair;
			}
		}
		return null;
	}
	
	public static LocalDateTime getCurrentLocalDateTime(){
		return LocalDateTime.now();
	}
	
	public static String genRptJobId(){
		String jobId = LocalDateTime.now().format(DateTimeFormatter.ofPattern(AppConstant.RPT_JOB_ID_PATTERN));
		return jobId;
	}
}
