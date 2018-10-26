package com.prototype.microservice.etl.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.tika.parser.txt.CharsetDetector;
import org.apache.tika.parser.txt.CharsetMatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.prototype.microservice.commons.error.CheckedException;
import com.prototype.microservice.etl.utils.RptMessageHelper;

@Component
public class CsvFileParser extends RptFileParser {
	@Autowired
	private RptRepository rptRepository;
	@Autowired
	private SqlAssembler columnAssembler;
	@Autowired
	private RptMessageHelper msgHelper;
	private String colDelimiter;
	private String currentLine;
	private int totalLineNum = 0;
	BufferedReader br = null;
	public static final String SUFFIX_CSV=".csv";
	public static final String SUFFIX_TXT=".txt";
	
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	@Override
	public void execNativeSql(String tableName,  List<ColumnMetaInfo> columnsInfo, List<String> values, Map<String, String> sysColValMap) throws Exception{
		String sql = columnAssembler.genNativeInsertSqlByColumnIndex(tableName, columnsInfo, values, sysColValMap, configInfo.getNullValFilter());
		//System.out.println(sql);
		try{
			if(StringUtils.isNotBlank(sql)){
				rptRepository.execUpdate(sql);				
			}
		}catch(Exception e){
			String msg = msgHelper.getMessage("RPT-ERR-003", new Object[]{"[CsvFileParser]", sql, e.getMessage()});
			RuntimeException e1 =  new RuntimeException(msg);
			throw e1;
		}
		//return sql;
	}
	public String exceParamiterlizedSql(String tableName,  List<ColumnMetaInfo> columnsInfo, List<String> values){
		String sql = columnAssembler.genInsertSqlWithParam(tableName, columnsInfo);
		System.out.println(sql);
		List<Object> valueObjList = columnAssembler.parseValues(columnsInfo, values);
		rptRepository.insertDataByParams(sql, valueObjList);
		return sql;
	}
	
	@Override
	public List<Integer> getColumnIndices() throws Exception{
		List<Integer> indices = new ArrayList<Integer>();
		if(configInfo!=null&&configInfo.getColumns()!=null){
			for(ColumnMetaInfo column:configInfo.getColumns()){
				indices.add(column.getColIndex());
			}
		}
		return indices;
	}
	@Override
	public List<String> parseColumnValues(Object row, List<ColumnMetaInfo> columns) throws Exception {
		List<String> valueStrList = new ArrayList<String>();
		if(row!=null && row instanceof String && columnIndices!=null){
			String line = (String) row;
			String[] values = line.split("\\"+colDelimiter);
			for(int i=0;i<values.length;i++){
				if(columnIndices.contains(i+1)){
					if(values[i].startsWith("\"")&&values[i].endsWith("\"")){
						values[i] = values[i].replaceFirst("^\"", "");
						values[i] = values[i].replaceFirst("\"$", "");
					}
					valueStrList.add(values[i]);
				}
			}
			//append null to the end
			if(values!=null && columnIndices.size()-values.length==1){
				valueStrList.add(null);
			}
			if(valueStrList.size()!=columnIndices.size()){
				throw new CheckedException(msgHelper.getMessage("RPT-ERR-002", new Object[]{colDelimiter}));
			}
			//trim
			for(String raw:valueStrList){
				if(raw!=null){
					raw = raw.trim();
				}
			}
		}
		return valueStrList;
	}
	@Override
	public boolean isBlankRow(Object row) {
		if(row!=null&&row instanceof String){
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
					return currentLine!=null?true:false;
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
		if(br!=null){
			try {
				br.close();
				br=null;
			} catch (IOException e) {
				rptLogger.error("Cannot close buffer reader");
				rptLogger.error(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	public static int countLines(InputStream in) throws IOException {
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
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
		Charset charset = Charset.forName(encoding);
		return charset;
	}
}
