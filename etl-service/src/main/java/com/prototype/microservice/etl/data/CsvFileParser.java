package com.prototype.microservice.etl.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import com.prototype.microservice.etl.meta.ColumnMetaInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.parser.txt.CharsetDetector;
import org.apache.tika.parser.txt.CharsetMatch;
import org.springframework.stereotype.Component;

import com.prototype.microservice.commons.error.CheckedException;

@Component
public class CsvFileParser extends EtlFileParser {
	private String colDelimiter;
	private String currentLine;
	private int totalLineNum = 0;
	private BufferedReader br = null;
//	public static final String SUFFIX_CSV=".csv";
//	public static final String SUFFIX_TXT=".txt";
	
	@Override
	public void init() {
//		rptLogger = LoggerFactory.getLogger(RptHelper.getLoggerName(configInfo.getFileNamePattern(), configInfo.getExecOnDate()));
		if(configInfo==null){
			return;
		}
		if(configInfo.getColumns()==null||configInfo.getColumns().size()<=0){
			return;
		}
		if(StringUtils.isBlank(configInfo.getColDelimiter())){
			return;
		}
		colDelimiter = configInfo.getColDelimiter();
//		if((file==null||!file.exists())&&in==null){
//			rptLogger.error(
//					MessageFormat.format("{0} Cannot found file : {1} under {2}", new Object[] { this.getClass().getName(), configInfo.getFileNamePattern(), configInfo.getFilePath()}));
//			return;
//		}
//		if(file!=null&&file.exists()){
//			try {
//				rptLogger.info("Parsing CSV/TXT file: "+file.getCanonicalPath());
//				totalLineNum = countLines(new FileInputStream(file));
//				br = new BufferedReader(new FileReader(file));
//			} catch (FileNotFoundException e) {
//				rptLogger.error(
//						MessageFormat.format("{0} Cannot found file : {1} under {2}", new Object[] { this.getClass().getName(), file.getName(), configInfo.getFilePath()}));
//				rptLogger.error(e.getMessage());
//				e.printStackTrace();
//			} catch (IOException e) {
//				rptLogger.error(e.getMessage());
//				e.printStackTrace();
//			}
//		}else 
		if(in!=null){
			try {
				charset = getFileEncoding();
				br = new BufferedReader(new InputStreamReader(in, charset));
			} catch (Exception e) {
				rptLogger.error(e.getMessage());
				e.printStackTrace();
			}
			try {
				in.mark(0);
				totalLineNum = countLines(in);
				in.reset(); //go back to the first line
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public void execNativeSql(String tableName, List<ColumnMetaInfo> columnsInfo, List<String> values, Map<String, String> sysColValMap){
		String sql = sqlAssembler.genNativeInsertSqlByColumnIndex(tableName, columnsInfo, values, sysColValMap, configInfo.getNullValFilter());
		//System.out.println(sql);
		try{
			if(StringUtils.isNotBlank(sql)){
				etlCommonRepository.execUpdate(sql);
			}
		}catch(Exception e){
			String msg = msgHelper.getMessage("RPT-ERR-003", new Object[]{"[CsvFileParser]", sql, e.getMessage()});
			throw new RuntimeException(msg);
		}
		//return sql;
	}

	public void execJpaSql(String tableName, List<ColumnMetaInfo> columnsInfo, List<String> values, Map<String, String> sysColValMap){
		String sql = sqlAssembler.genInsertSqlWithParam(tableName, columnsInfo);
		try{
			if(StringUtils.isNotBlank(sql)){
				etlCommonRepository.insertDataByParams(sql, values);
			}
		}catch(Exception e){
			String msg = msgHelper.getMessage("RPT-ERR-003", new Object[]{"[CsvFileParser]", sql, e.getMessage()});
			throw new RuntimeException(msg);
		}
	}

	@Override
	public List<Integer> getColumnIndices(){
		List<Integer> indices = new ArrayList<>();
		if(configInfo!=null&&configInfo.getColumns()!=null){
			IntStream.range(0, configInfo.getColumns().size()).forEach(i->indices.add(i+1));
		}
		return indices;
	}
	@Override
	public List<String> parseColumnValues(Object row, List<ColumnMetaInfo> columns) throws Exception {
		List<String> valueStrList = new ArrayList<>();
		if(row instanceof String && columnIndices!=null){
			String line = (String) row;
            String pattern = new StringBuilder().append("\\").append(colDelimiter)
                    .append("(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)").toString();
            String[] values = line.split(pattern);
			for(int i=0;i<values.length;i++){
				if(columnIndices.contains(i+1)){
					if(values[i].startsWith("\"")&&values[i].endsWith("\"")){
						values[i] = values[i].replaceFirst("^\"", "");
						values[i] = values[i].replaceFirst("\"$", "");
					}
					valueStrList.add(values[i].trim());
				}
			}
			//append null to the end
			if(columnIndices.size()-values.length==1){
				valueStrList.add(null);
			}
			if(valueStrList.size()!=columnIndices.size()) {
				System.out.println(valueStrList);
				throw new CheckedException(msgHelper.getMessage("RPT-ERR-002", new Object[]{colDelimiter}));
			}
		}
		return valueStrList;
	}
	@Override
	public boolean isBlankRow(Object row) {
		if(row instanceof String){
			String line = (String) row;
			return StringUtils.isBlank(line.trim());
		}
		return true;
	}
	@Override
	public boolean hasNextRow() {
		if(br!=null){
			try {
				if(currentRowIndex<totalLineNum-configInfo.getEndRowIndexFromBottom()){
					currentLine = br.readLine();
					return currentLine != null;
				}else{
					return false;
				}
			} catch (IOException e) {
				rptLogger.error(e.getMessage());
				e.printStackTrace();
			}
		}
		return false;
	}
	@Override
	public Object getRow() {
		return currentLine;
	}
	@Override
	public void clean() {
		etlCommonRepository.closeDbConn();
		currentRowIndex=0;
		currentLine=null;
		colDelimiter=null;
		if(in!=null){
			try {
				in.reset();
				in.close();//go back to the first line
			} catch (IOException e) {
				rptLogger.error("Cannot close input stream.");
				rptLogger.error(e.getMessage());
				e.printStackTrace();
			}
		}
		if(br!=null) try {
			br.close();
			br = null;
		} catch (IOException e) {
			rptLogger.error("Cannot close buffer reader");
			rptLogger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	private static int countLines(InputStream in) throws IOException {
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars;
	        boolean empty = true;
	        while ((readChars = in.read(c)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n') {
	                    ++count;
	                }
	            }
	        }
	        return (count == 0 && !empty) ? 1 : count+1;
	    } finally {
	    	in.close();
	    }
	}
	@Override
	public Charset getFileEncoding(){
		String encoding = "UTF-8";
		if(StringUtils.isNotBlank(configInfo.getFileEncoding())){
			encoding = configInfo.getFileEncoding();
			charset = Charset.forName(encoding);
		}else{
			InputStream inForCharset = null;
			int threshold = 35;
			try{
				CharsetDetector charsetDetector = new CharsetDetector();
				if(in!=null){
					inForCharset = in;
				}
				assert inForCharset != null;
				inForCharset.mark(0);
				charsetDetector.setText(in);
				CharsetMatch match = charsetDetector.detect();

			    if(match != null && match.getConfidence() > threshold){
			    	encoding = match.getName();
			    }
			    inForCharset.reset();
				inForCharset.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		return Charset.forName(encoding);
	}
}
