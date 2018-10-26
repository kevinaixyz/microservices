package com.prototype.microservice.etl;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;

import com.prototype.microservice.etl.constant.ReadInvPosition;

public class TestEncrption {

	
	@Autowired
	private INVStrategyAService invStrategyAService;
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword("prototype");
		encryptor.setStringOutputType("hexadecimal");// we HAVE TO set a password
		//encryptor.setAlgorithm("PBEWithMD5AndTripleDES");    // optionally set the algorithm
		System.out.println(encryptor.encrypt("cdm5w1n5")+"\n");
		System.out.println(encryptor.decrypt("CDC34B34E33FD4FE9F212987EB85B04B8A673CC2EE8C093B"));
		
		
		// TODO Auto-generated method stub
				 DataFormatter dataFormatter = new DataFormatter();
				 String fileName = "INV 协管资产_20180615 End of Day.xlsx";
				String path = "C:\\backup\\520\\inv excel\\"+fileName;
				
				
				String Strategy="";
				String category="";
				String fund="";
				 try {
					Workbook workbook = WorkbookFactory.create(new File(path));
					Sheet sheet = workbook.getSheetAt(0);
					/*Iterator<Row> rowIterator = sheet.rowIterator();
			        while (rowIterator.hasNext()) {
			            Row row = rowIterator.next();

			            // Now let's iterate over the columns of the current row
			            Iterator<Cell> cellIterator = row.cellIterator();

			            while (cellIterator.hasNext()) {
			                Cell cell = cellIterator.next();
			                String cellValue = dataFormatter.formatCellValue(cell);
			                System.out.print(cellValue+" "+cell.getColumnIndex() + "\t");
			                //System.out.print(cellValue + "\t");
			            }
			            System.out.println();
			        }*/
					Row row = sheet.getRow(4);
					Cell cell = row.getCell(0);
					Cell cell2 = row.getCell(1);
					Cell cell3 = row.getCell(2);
					Cell cell4 = row.getCell(3);
					Cell cell5 = row.getCell(13);
					
					System.out.println(new DecimalFormat("#0.0000000#").format( Double.valueOf(cell5.getNumericCellValue())));
					List<INVStrategyBDTO> list = ReadInvPosition.parseExcelStrategyB(4, 0, workbook);
					String asOfDate = fileName.split("_")[1].substring(0, 8);
					TestEncrption test = new TestEncrption();
					
					
				 }catch(Exception e)
				 {
					 e.printStackTrace();
				 }
	}
	
	public void test(String tenantId,List<INVStrategyADTO> list,String asOfDate)
	{
		invStrategyAService.saveINVStrategyA(tenantId, list, asOfDate);
	}

}
