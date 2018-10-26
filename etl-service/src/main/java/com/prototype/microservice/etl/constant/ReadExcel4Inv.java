package com.prototype.microservice.etl.constant;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;

public class ReadExcel4Inv {

	/*
	 * public static List<Object> getDataFromExlce(String excelBase64String,
	 * String filePath) throws IOException { InputStream inputStream = null; try
	 * { String fileStr = excelBase64String; byte[] decode =
	 * Base64.decodeBase64(fileStr); InputStream fileInputStream = new
	 * ByteArrayInputStream(decode); Workbook workbook = new
	 * HSSFWorkbook(fileInputStream); Sheet worksheet = workbook.getSheetAt(0);
	 * String result = "";
	 * 
	 * Properties prop = new Properties();
	 * 
	 * String propFileName = filePath;
	 * 
	 * // inputStream = //
	 * getClass().getClassLoader().getResourceAsStream(propFileName); File
	 * properfiesFile = new File(AppConstant.PROP_FILE_LOCATION_CMDS +
	 * propFileName); inputStream = new FileInputStream(properfiesFile);
	 * 
	 * if (inputStream != null) { prop.load(inputStream); } // else { // throw
	 * new FileNotFoundException("property file '" + // propFileName +
	 * "' not found in the classpath"); // }
	 * 
	 * // get the property value and print it out // String user =
	 * prop.getProperty("user"); // String company1 =
	 * prop.getProperty("company1"); // String company2 =
	 * prop.getProperty("company2"); // String company3 =
	 * prop.getProperty("company3"); String worksheetInfo =
	 * prop.getProperty("worksheetInfo").toString();
	 * 
	 * } catch (Exception e) { System.out.println("Exception: " + e);
	 * e.printStackTrace(); } finally { inputStream.close(); } return null; }
	 */

	public static List<Object> parseExcel(String propFileName, int worksheetNumber, Workbook workbook) {
		List<Object> list = new ArrayList<Object>();
		int count = 0;
		try {

			Sheet worksheet = workbook.getSheetAt(worksheetNumber);
			FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
			InputStream inputStream = null;
			Properties prop = new Properties();
			try {

				// inputStream =
				// getClass().getClassLoader().getResourceAsStream(propFileName);
				File properfiesFile = new File(
						AppConstant.PROP_FILE_LOCATION_CMDS + propFileName + worksheetNumber + ".properties");
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

			int startPosition = Integer.parseInt(prop.getProperty("tableCellStartPosition"));
			String tableCell = prop.getProperty("tableCell");

			Iterator<Row> iterator = worksheet.iterator();
			while (iterator.hasNext()) {
				Row currentRow = iterator.next();
				int firstCol = 0;
				if (currentRow.getRowNum() > startPosition) {

					Cell cellCheck = currentRow.getCell((short) Integer.parseInt("0"));

					String cellCheckValue = "";
					if (cellCheck != null) {
						if (cellCheck.getCellType() == XSSFCell.CELL_TYPE_STRING) {
							cellCheckValue = cellCheck.getStringCellValue();
						} else if (cellCheck.getCellType() == XSSFCell.CELL_TYPE_ERROR) {
							Byte byteVal = cellCheck.getErrorCellValue();
							cellCheckValue = byteVal.toString();
						} else if (cellCheck.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
							Double doubleVal = cellCheck.getNumericCellValue();
							cellCheckValue = doubleVal.toString();
						} else if (cellCheck.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {
							Boolean booleanVal = cellCheck.getBooleanCellValue();
							cellCheckValue = booleanVal.toString();
						} else if (cellCheck.getCellType() == XSSFCell.CELL_TYPE_FORMULA) {
							try{
							cellCheckValue = evaluator.evaluate(cellCheck).formatAsString();
							}catch(Exception e)
							{
								System.out.println(evaluator.evaluate(cellCheck)+"$$$$$$$$$$$$$$");
								e.printStackTrace();
							}
						}
					}

					if (cellCheckValue != null && !cellCheckValue.equals("")) {

						String qualifiedClassName = prop.getProperty("tableCellDTO");
						Class ledgBalTnVoClass = Class.forName(qualifiedClassName);

						Object ledgBalTnVo = ledgBalTnVoClass.newInstance();

						String[] cellStrInfo = tableCell.split("\\|\\|");
						for (int i = 0; i < cellStrInfo.length; i++) {
							String cellObjInfo = cellStrInfo[i];
							String[] info = cellObjInfo.split(",");
							Cell cell = currentRow.getCell((short) Integer.parseInt(info[0]));
							String methodName = "get" + info[1].substring(0, 1).toUpperCase() + info[1].substring(1);
							SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
							SimpleDateFormat timeSdf = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
							DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
							DateFormat dateFormat2 = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

							if (cell != null) {
								count++;
								Method method1 = null;
								if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING
										&& methodName.contains("NumericCellValue")) {
									method1 = cell.getClass().getMethod("getStringCellValue", new Class[] {});
								} else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC
										&& methodName.contains("StringCellValue")) {
									method1 = cell.getClass().getMethod("getNumericCellValue", new Class[] {});
								} else if (cell.getCellType() == XSSFCell.CELL_TYPE_ERROR
										&& methodName.contains("StringCellValue")) {
									method1 = cell.getClass().getMethod("getErrorCellValue", new Class[] {});
								} else if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING
										&& methodName.contains("DateCellValue")) {
									method1 = cell.getClass().getMethod("getStringCellValue", new Class[] {});
								}else {
									method1 = cell.getClass().getMethod(methodName, new Class[] {});
								}
								if (count == 353) {
									System.out.print(methodName);
								}
								String fieldValue = "";
								if (cell.getCellType() != 2) {
									if(methodName.equals("getNumericCellValue"))
									{
										//Long value = Double.valueOf(method1.invoke(cell, new Class[] {}).toString()).longValue();
										try{
											if( method1.invoke(cell, new Class[] {})!=null)
											{
												fieldValue = new DecimalFormat("#0.0000000#").format( Double.valueOf(method1.invoke(cell, new Class[] {}).toString()));
												
											}
										}catch(Exception ek)
										{
											ek.printStackTrace();
										}
									}
									else
									{
										if( method1.invoke(cell, new Class[] {})!=null)
										{
											fieldValue = method1.invoke(cell, new Class[] {}).toString();
										}
									}
								} else {
									try {
										fieldValue = evaluator.evaluate(cell).formatAsString();
										if(fieldValue.startsWith("\"")&&fieldValue.endsWith("\""))
										{
											fieldValue = fieldValue.substring(1, fieldValue.length()-1);
										}
									} catch (Exception e) {
										System.out.println(e + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
										if (e.toString().contains("java.lang.IllegalArgumentException")) {
											System.out.println(count + "|||||||||||||||||||||||||||||");
										}

									}
								}

								String setMethodName = "set" + info[3].substring(0, 1).toUpperCase()
										+ info[3].substring(1);
								if (info[2].toString().equals("String")) {
									Method setMethod = ledgBalTnVo.getClass().getMethod(setMethodName, String.class);
									try {
										setMethod.invoke(ledgBalTnVo, new String[] { fieldValue });

									} catch (Exception e3) {
										System.out.print(e3);
										e3.printStackTrace();
									}
								} else if (info[2].toString().equals("BigDecimal")) {
									Method setMethod = ledgBalTnVo.getClass().getMethod(setMethodName,
											BigDecimal.class);
									try {
										if (fieldValue != null && !fieldValue.equals("")) {
											boolean isNumeric = fieldValue.replace(",", "").replace(".", "").replace("-", "").chars()
													.allMatch(Character::isDigit);
											if (isNumeric && !fieldValue.replace(",", "").replace(".", "").equals("")) {
												setMethod.invoke(ledgBalTnVo,
														new BigDecimal[] { new BigDecimal(fieldValue) });
											}
										}

									} catch (Exception e3) {
										System.out.print(e3 + "^^^^^^^^^^^^^^^^^^^^^^^^^^^^" + count);
										e3.printStackTrace();
									}
								} else if (info[2].toString().equals("Date")) {
									Method setMethod = ledgBalTnVo.getClass().getMethod(setMethodName, Date.class);
									try {
										if (fieldValue != null && !fieldValue.equals("")) {
											try {
												Date date = dateFormat.parse(fieldValue);
												setMethod.invoke(ledgBalTnVo,
														new Date[] { sdf.parse(sdf.format(date)) });

											} catch (Exception ex) {
												// do something for invalid
												// dateformat

												try
												{
													setMethod.invoke(ledgBalTnVo, new Date[] { sdf.parse(fieldValue) });
												}
												catch(Exception e2)
												{
													try
													{
														setMethod.invoke(ledgBalTnVo, new Date[] { dateFormat2.parse(fieldValue) });
													}
													catch(Exception e3)
													{
														boolean isNumeric = fieldValue.replace(".", "").chars()
																.allMatch(Character::isDigit);
														if (isNumeric) {
															 Date javaDate= DateUtil.getJavaDate(Double.parseDouble(fieldValue));
															 setMethod.invoke(ledgBalTnVo,
																		new Date[] { sdf.parse(sdf.format(javaDate)) });
														}
													}
												}
												
											}
										}

									} catch (Exception e3) {
										System.out.print(e3 + "***********************");
										e3.printStackTrace();
									}
								} else if (info[2].toString().equals("Time")) {
									Method setMethod = ledgBalTnVo.getClass().getMethod(setMethodName, Date.class);
									try {
										if (fieldValue != null && !fieldValue.equals("")) {
											boolean isNumeric = fieldValue.replaceAll(":", "").chars()
													.allMatch(Character::isDigit);
											if (isNumeric) {
												setMethod.invoke(ledgBalTnVo, new Date[] { timeSdf.parse(fieldValue) });
											}
										}

									} catch (Exception e3) {
										System.out.print(e3 + "========================");
										e3.printStackTrace();
									}
								}
							}

						}

						if (prop.getProperty("specifyCell4TableCell") != null
								&& !prop.getProperty("specifyCell4TableCell").equals("")) {
							String specifyCell4TableCellStr = prop.getProperty("specifyCell4TableCell");
							String[] specifyCellInfo = specifyCell4TableCellStr.split(",");
							Cell cell = worksheet.getRow(Integer.parseInt(specifyCellInfo[0]))
									.getCell(Integer.parseInt(specifyCellInfo[1]));
							String methodName = "get" + specifyCellInfo[2].substring(0, 1).toUpperCase()
									+ specifyCellInfo[2].substring(1);

							Method method1 = cell.getClass().getMethod(methodName, new Class[] {});
							String fieldValue = method1.invoke(cell, new Class[] {}).toString();
							String dateStr = fieldValue.replace("Account Balance List to SFC - Summery in HKD Date:",
									"");
							SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
							// Date date = sdf.parse(dateStr);

							String setMethodName = "set" + specifyCellInfo[4].substring(0, 1).toUpperCase()
									+ specifyCellInfo[4].substring(1);

							if (specifyCellInfo[3].toString().equals("String")) {
								Method setMethod = ledgBalTnVo.getClass().getMethod(setMethodName, String.class);
								try {
									setMethod.invoke(ledgBalTnVo, new String[] { fieldValue });

								} catch (Exception e3) {
									System.out.print(e3);
								}
							} else if (specifyCellInfo[3].toString().equals("BigDecimal")) {
								Method setMethod = ledgBalTnVo.getClass().getMethod(setMethodName, double.class);
								try {
									setMethod.invoke(ledgBalTnVo, new BigDecimal[] { new BigDecimal(fieldValue) });

								} catch (Exception e3) {
									System.out.print(e3);
								}
							} else if (specifyCellInfo[3].toString().equals("Date")) {
								Method setMethod = ledgBalTnVo.getClass().getMethod(setMethodName, Date.class);
								try {
									setMethod.invoke(ledgBalTnVo, new Date[] { sdf.parse(dateStr) });

								} catch (Exception e3) {
									System.out.print(e3);
								}
							}

						}

						list.add(ledgBalTnVo);
					}
					// ledgBalTnVo.setDate(date);
					// readExcelData(currentRow, ledgBalTnVo);
					// ledgBalTnVoList.add(ledgBalTnVo);
				}
			}

		} catch (Exception e) {

			// TODO Auto-generated catch block
			System.out.println(count + "=============================");
			e.printStackTrace();
		}
		return list;
	}

}
