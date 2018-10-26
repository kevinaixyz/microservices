//package com.prototype.microservice.cms.batch.mapper;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//
//import org.apache.poi.hssf.usermodel.HSSFDateUtil;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellType;
//import org.apache.poi.ss.usermodel.Row;
//
//import com.prototype.microservice.cms.entity.CmsExcelRow;
//import com.prototype.microservice.cms.utils.CmsHelper;
//import org.springframework.beans.factory.annotation.Autowired;
//
//public class CmsRowMapper implements PoiRowMapper<CmsExcelRow> {
//	@Autowired
//	CmsHelper cmsHelper;
//
//	@Override
//	public CmsExcelRow mapRow(Row row) {
//		boolean isBlank=true;
//		CmsHelper cmsHelper = CmsHelper.getInstance();
//
//		int colIndex=0;
//		String fieldName="";
//		if(row!=null){
//			for(Cell cell:row){
//				if(cell.getCellTypeEnum()!=CellType.BLANK){
//					isBlank = false;
//					break;
//				}
//			}
//		}
//		if(isBlank){
//			return null;
//		}
//		CmsExcelRow row = new CmsExcelRow();
//		Field[] fields = row.getClass().getDeclaredFields();
//		if(row!=null&&row.getLastCellNum()==fields.length){
//			try{
//				for(int i=0;i<row.getLastCellNum();i++){
//					colIndex = i;
//					fieldName=fields[i].getName();
//					String setterName = "set"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1, fieldName.length());
//					Method method = row.getClass().getMethod(setterName, String.class);
//					//fieldValue = row.getCell(i).g
//					CellType cellType = row.getCell(i).getCellTypeEnum();
//					if(cellType==CellType.STRING){
//						method.invoke(row, row.getCell(i).getStringCellValue());
//					}
//					else if(HSSFDateUtil.isCellDateFormatted(row.getCell(i))){
//						method.invoke(row, cmsHelper.formatRawTradeDate(row.getCell(i).getDateCellValue()));
//					}else if(cellType==CellType.FORMULA){
//						method.invoke(row, row.getCell(i).getCellFormula());
//					}else if (cellType == CellType.NUMERIC){
//						method.invoke(row, String.valueOf(row.getCell(i).getNumericCellValue()));
//					}
//				}
//			}catch(Exception e){
//				e.printStackTrace();
//				throw new RuntimeException("Error when setting colums value @Colum:"+colIndex+":"+fieldName);
//			}
//		}
//		return row;
//	}
//
//
//}
