package com.prototype.microservice.etl.constant;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;

public class ReadInvPosition {

	public static List<INVStrategyADTO> parseExcel(int rowNumber,int worksheetNumber, Workbook workbook) {
	
		List<INVStrategyADTO> invStrategyADTOList = new ArrayList<INVStrategyADTO>();
		
		Sheet workSheet = workbook.getSheetAt(worksheetNumber);
		
		String strategy="";
	    String category="";
	    String fund="";
		for(int i=rowNumber;i<=workSheet.getLastRowNum();i++)
		{
			INVStrategyADTO dto = new INVStrategyADTO();
			Row row = workSheet.getRow(i);
			Cell cell = row.getCell(0);
			Cell cell1 = row.getCell(1);
			Cell cell2 = row.getCell(2);
			Cell cell3 = row.getCell(3);
			Cell cell4 = row.getCell(4);
			Cell cell5 = row.getCell(5);
			Cell cell6 = row.getCell(6);
			Cell cell7 = row.getCell(7);
			Cell cell8 = row.getCell(8);
			Cell cell9 = row.getCell(9);
			Cell cell10 = row.getCell(10);
			Cell cell11 = row.getCell(11);
			Cell cell12 = row.getCell(12);
			Cell cell13 = row.getCell(13);
			Cell cell14 = row.getCell(14);
			Cell cell15 = row.getCell(15);
			Cell cell16 = row.getCell(16);
			Cell cell17 = row.getCell(17);
			Cell cell18 = row.getCell(18);
			Cell cell19 = row.getCell(19);
			
			if(cell!=null)
			{
				strategy = cell.getStringCellValue().replace("Strategy:", "").trim();
			}
			
			if(cell1!=null&&!cell1.getStringCellValue().equals(""))
			{
				category = cell1.getStringCellValue().replace("*Category:",  "").trim();
			}
			
			if(cell2!=null&&!cell2.getStringCellValue().equals(""))
			{
				fund = cell2.getStringCellValue().replace("Fund:",  "").trim();
			}
			
			System.out.print("============");
			
			dto.setStrategy(strategy);
			dto.setCategory(category);
			dto.setFund(fund);
			
			if(cell3!=null)
			{
				dto.setTicker(cell3.getStringCellValue());
			}
			
			if(cell4!=null)
			{
				dto.setSecurities(cell4.getStringCellValue());
			}
			
			if(cell5!=null)
			{
				dto.setCashClass(cell5.getStringCellValue());
			}
			
			if(cell6!=null&&cell6.getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
			{
				
				dto.setPercentageOfValue(new BigDecimal(new DecimalFormat("#0.0000000#").format( Double.valueOf(cell6.getNumericCellValue()))));
			}
			
			if(cell7!=null&&cell7.getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
			{
				
				dto.setValueHKD(new BigDecimal(new DecimalFormat("#0.0000000#").format( Double.valueOf(cell7.getNumericCellValue()))));
			}
			
			if(cell8!=null&&cell8.getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
			{
				
				dto.setAmount(new BigDecimal(new DecimalFormat("#0.0000000#").format( Double.valueOf(cell8.getNumericCellValue()))));
			}
			
			if(cell9!=null&&cell9.getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
			{
				
				dto.setPriceMove(new BigDecimal(new DecimalFormat("#0.0000000#").format( Double.valueOf(cell9.getNumericCellValue()))));
			}
			
			if(cell10!=null&&cell10.getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
			{
				
				dto.setValueUSD(new BigDecimal(new DecimalFormat("#0.0000000#").format( Double.valueOf(cell10.getNumericCellValue()))));
			}
			
			if(cell11!=null&&cell11.getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
			{
				
				dto.setTdPNLHKD(new BigDecimal(new DecimalFormat("#0.0000000#").format( Double.valueOf(cell11.getNumericCellValue()))));
			}
			
			if(cell12!=null&&cell12.getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
			{
				
				dto.setUnrealisedPNL(new BigDecimal(new DecimalFormat("#0.0000000#").format( Double.valueOf(cell12.getNumericCellValue()))));
			}
			
			if(cell13!=null&&cell13.getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
			{
				
				dto.setUnrealisedPNLPercentage(new BigDecimal(new DecimalFormat("#0.0000000#").format( Double.valueOf(cell13.getNumericCellValue()))));
			}
			
			if(cell14!=null&&cell14.getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
			{
				
				dto.setPrice(new BigDecimal(new DecimalFormat("#0.0000000#").format( Double.valueOf(cell14.getNumericCellValue()))));
			}
			
			if(cell15!=null&&cell15.getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
			{
				
				dto.setCps(new BigDecimal(new DecimalFormat("#0.0000000#").format( Double.valueOf(cell15.getNumericCellValue()))));
			}
			
			
			/*
			if(cell12!=null&&cell12.getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
			{
				
				dto.setValueUSD(new BigDecimal(new DecimalFormat("#0.0000000#").format( Double.valueOf(cell12.getNumericCellValue()))));
			}
			
			if(cell13!=null&&cell13.getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
			{
				
				dto.setTdPNLHKD(new BigDecimal(new DecimalFormat("#0.0000000#").format( Double.valueOf(cell13.getNumericCellValue()))));
			}
			if(cell15!=null&&cell15.getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
			{
				
				dto.setTotalPNL(new BigDecimal(new DecimalFormat("#0.0000000#").format( Double.valueOf(cell15.getNumericCellValue()))));
			}
			if(cell16!=null&&cell16.getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
			{
				
				dto.setTotalPNLPercentage(new BigDecimal(new DecimalFormat("#0.0000000#").format( Double.valueOf(cell16.getNumericCellValue()))));
			}
			
			if(cell17!=null&&cell17.getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
			{
				
				dto.setPrice(new BigDecimal(new DecimalFormat("#0.0000000#").format( Double.valueOf(cell17.getNumericCellValue()*100))));
			}
			
			if(cell19!=null&&cell19.getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
			{
				
				dto.setCps(new BigDecimal(new DecimalFormat("#0.0000000#").format( Double.valueOf(cell19.getNumericCellValue()))));
			}*/
			invStrategyADTOList.add(dto);
			System.out.println("========");
		}
		
		
		
		return invStrategyADTOList;
	}
	
	
	public static List<INVStrategyBDTO> parseExcelStrategyB(int rowNumber,int worksheetNumber, Workbook workbook) {
		
		List<INVStrategyBDTO> invStrategyBDTOList = new ArrayList<INVStrategyBDTO>();
		
		Sheet workSheet = workbook.getSheetAt(worksheetNumber);
		
		String strategy="";
	    String category="";
	    String fund="";
		for(int i=rowNumber;i<=(workSheet.getLastRowNum());i++)
		{
			INVStrategyBDTO dto = new INVStrategyBDTO();
			Row row = workSheet.getRow(i);
			Cell cell = row.getCell(0);
			Cell cell1 = row.getCell(1);
			Cell cell2 = row.getCell(2);
			Cell cell3 = row.getCell(3);
			Cell cell4 = row.getCell(4);
			Cell cell5 = row.getCell(5);
			Cell cell6 = row.getCell(6);
			Cell cell7 = row.getCell(7);
			Cell cell8 = row.getCell(8);
			Cell cell9 = row.getCell(9);
			Cell cell10 = row.getCell(10);
			Cell cell11 = row.getCell(11);
			Cell cell12 = row.getCell(12);
			Cell cell13 = row.getCell(13);
			Cell cell14 = row.getCell(14);
			Cell cell15 = row.getCell(15);
			Cell cell16 = row.getCell(16);
			Cell cell17 = row.getCell(17);
			Cell cell18 = row.getCell(18);
			Cell cell19 = row.getCell(19);
			Cell cell20 = row.getCell(20);
			Cell cell21 = row.getCell(21);
			if(cell!=null)
			{
				strategy = cell.getStringCellValue().replace("Strategy:", "").trim();
			}
			
			if(cell1!=null&&!cell1.getStringCellValue().equals(""))
			{
				category = cell1.getStringCellValue().replace("Category:",  "").trim();
			}
			
			if(cell2!=null&&!cell2.getStringCellValue().equals(""))
			{
				fund = cell2.getStringCellValue().replace("Fund:",  "").trim();
			}
			
			System.out.print("============");
			
			dto.setStrategy(strategy);
			dto.setCategory(category);
			dto.setFund(fund);
			
			if(cell3!=null)
			{
				dto.setTicker(cell3.getStringCellValue());
			}
			
			if(cell4!=null)
			{
				dto.setDescription(cell4.getStringCellValue());
			}
			
			if(cell5!=null)
			{
				dto.setCashClasss(cell5.getStringCellValue());
			}
			
			
			if(cell6!=null&&cell6.getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
			{
				
				dto.setPercentageOfValue(new BigDecimal(new DecimalFormat("#0.0000000#").format( Double.valueOf(cell6.getNumericCellValue()))));
			}
			
			
			if(cell7!=null&&cell7.getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
			{
				
				dto.setAmount(new BigDecimal(new DecimalFormat("#0.0000000#").format( Double.valueOf(cell7.getNumericCellValue()))));
			}
			
			if(cell8!=null&&cell8.getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
			{
				
				dto.setPriceMove(new BigDecimal(new DecimalFormat("#0.0000000#").format( Double.valueOf(cell8.getNumericCellValue()))));
			}
			
			if(cell9!=null&&cell9.getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
			{
				
				dto.setValueHKD(new BigDecimal(new DecimalFormat("#0.0000000#").format( Double.valueOf(cell9.getNumericCellValue()))));
			}
			
			
			if(cell10!=null&&cell10.getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
			{
				
				dto.setValueCNY(new BigDecimal(new DecimalFormat("#0.0000000#").format( Double.valueOf(cell10.getNumericCellValue()))));
			}
			
			if(cell11!=null&&cell11.getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
			{
				
				dto.setValueUSD(new BigDecimal(new DecimalFormat("#0.0000000#").format( Double.valueOf(cell11.getNumericCellValue()))));
			}
			
			if(cell12!=null&&cell12.getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
			{
				
				dto.setTdPNLHKD(new BigDecimal(new DecimalFormat("#0.0000000#").format( Double.valueOf(cell12.getNumericCellValue()))));
			}
			if(cell13!=null&&cell13.getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
			{
				
				dto.setTotalPNL(new BigDecimal(new DecimalFormat("#0.0000000#").format( Double.valueOf(cell13.getNumericCellValue()))));
			}
			if(cell14!=null&&cell14.getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
			{
				
				dto.setTotalPNLPercentage(new BigDecimal(new DecimalFormat("#0.0000000#").format( Double.valueOf(cell14.getNumericCellValue()))));
			}
			
			if(cell15!=null&&cell15.getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
			{
				
				dto.setPrice(new BigDecimal(new DecimalFormat("#0.0000000#").format( Double.valueOf(cell15.getNumericCellValue()))));
			}
			
			if(cell16!=null&&cell16.getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
			{
				
				dto.setCps(new BigDecimal(new DecimalFormat("#0.0000000#").format( Double.valueOf(cell16.getNumericCellValue()))));
			}
			
			invStrategyBDTOList.add(dto);
			System.out.println("========");
		}
		
		
		
		return invStrategyBDTOList;
	}
}
