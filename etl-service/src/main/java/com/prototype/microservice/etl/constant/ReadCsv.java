package com.prototype.microservice.etl.constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class ReadCsv {
	
	public static List<Object> parseCsv(String propFileName, Reader reader ) {
		List<Object> list = new ArrayList<Object>();
		try {
			 	CSVReader csvReader =  new CSVReaderBuilder(reader).withSkipLines(1).build();
				//Sheet worksheet = workbook.getSheetAt(worksheetNumber);
				InputStream inputStream = null;
				Properties prop = new Properties();
				try {

					// inputStream =
					// getClass().getClassLoader().getResourceAsStream(propFileName);
					File properfiesFile = new File(AppConstant.PROP_FILE_LOCATION_CMDS + propFileName );
					inputStream = new FileInputStream(properfiesFile);

					if (inputStream != null) {
						prop.load(inputStream);
					} // else {
						// throw new FileNotFoundException("property file '" +
						// propFileName + "' not found in the classpath");
						// }

					// get the property value and print it out
					// String user = prop.getProperty("user");
					// String company1 = prop.getProperty("company1");
					// String company2 = prop.getProperty("company2");
					// String company3 = prop.getProperty("company3");

				} catch (Exception e) {
					System.out.println("Exception: " + e);
				} finally {
					inputStream.close();
				}

				

				//int startPosition = Integer.parseInt(prop.getProperty("tableCellStartPosition"));
				String tableCell = prop.getProperty("tableCell");

				//Iterator<Row> iterator = worksheet.iterator();
				//while (iterator.hasNext()) {
			    String[] nextRecord;
	            while ((nextRecord = csvReader.readNext()) != null) {
					  //Row currentRow = iterator.next();
					//if (currentRow.getRowNum() > startPosition) {
						String qualifiedClassName = prop.getProperty("tableCellDTO");
						Class ledgBalTnVoClass = Class.forName(qualifiedClassName);
						Object ledgBalTnVo = ledgBalTnVoClass.newInstance();
						
						String column1 = nextRecord[0];
						
						if(column1!=null&&!column1.trim().equals(""))
						{
						
						String[] cellStrInfo = tableCell.split("\\|\\|");
						for (int i = 0; i < cellStrInfo.length; i++) {
							String cellObjInfo = cellStrInfo[i];
							String[] info = cellObjInfo.split(",");
							//Cell cell = currentRow.getCell((short) Integer.parseInt(info[0]));
							//String methodName = "get" + info[1].substring(0, 1).toUpperCase() + info[1].substring(1);
							SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy", Locale.ENGLISH);
							SimpleDateFormat timeSdf = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
							
							
							if(nextRecord!=null&&nextRecord[0]!=null&&!nextRecord[0].equals("")&&nextRecord.length==cellStrInfo.length)
							{
								//Method method1 = cell.getClass().getMethod(methodName, new Class[] {});
								String fieldValue = nextRecord[Integer.parseInt(info[0])];
	
								String setMethodName = "set" + info[3].substring(0, 1).toUpperCase() + info[3].substring(1);
								if (info[2].toString().equals("String")) {
									Method setMethod = ledgBalTnVo.getClass().getMethod(setMethodName, String.class);
									try {
										setMethod.invoke(ledgBalTnVo, new String[] { fieldValue });
	
									} catch (Exception e3) {
										System.out.print(e3);
										e3.printStackTrace();
									}
								} else if (info[2].toString().equals("BigDecimal")) {
									Method setMethod = ledgBalTnVo.getClass().getMethod(setMethodName, BigDecimal.class);
									try {
										if(!fieldValue.replace("NA", "").trim().equals(""))
										{
											if(fieldValue.contains("(")&&fieldValue.contains(")"))
											{
												String value = fieldValue.replace(",", "").replace("(", "").replace(")", "").replace(".", "").trim();
												boolean isNumeric = value.chars().allMatch( Character::isDigit );
												if(isNumeric)
												setMethod.invoke(ledgBalTnVo, new BigDecimal[] { new BigDecimal(fieldValue.replace(",", "").replace("(", "-").replace(")", "").trim()) });
											}
											else
											{
												boolean isNumeric = fieldValue.replace(",", "").replace(".", "").chars().allMatch( Character::isDigit );
												if(isNumeric)
												setMethod.invoke(ledgBalTnVo, new BigDecimal[] { new BigDecimal(fieldValue.replace(",", "")) });
											}
										
										}
	
									} catch (Exception e3) {
										System.out.print(e3+" ---- "+fieldValue);
									}
								} else if (info[2].toString().equals("Date")) {
									Method setMethod = ledgBalTnVo.getClass().getMethod(setMethodName, Date.class);
									try {
										if(info.length==5)
										{
											sdf = new SimpleDateFormat(info[4], Locale.ENGLISH);
										}
										if(!fieldValue.replace("NA", "").trim().equals(""))
										{
										setMethod.invoke(ledgBalTnVo, new Date[] { sdf.parse(fieldValue) });
										}
	
									} catch (Exception e3) {
										System.out.print(e3);
									}
								} else if (info[2].toString().equals("Time")) {
									Method setMethod = ledgBalTnVo.getClass().getMethod(setMethodName, Date.class);
									try {
										if(info.length==5)
										{
											timeSdf = new SimpleDateFormat(info[4], Locale.ENGLISH);
										}
										setMethod.invoke(ledgBalTnVo, new Date[] { timeSdf.parse(fieldValue) });
	
									} catch (Exception e3) {
										System.out.print(e3);
									}
								}
							}

						}

						

						list.add(ledgBalTnVo);
						}
						// ledgBalTnVo.setDate(date);
						// readExcelData(currentRow, ledgBalTnVo);
						// ledgBalTnVoList.add(ledgBalTnVo);
					//}
				}

			
		} catch (Exception e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}
