package com.prototype.microservice.etl.data;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.prototype.microservice.etl.meta.ColumnMetaInfo;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;

import com.prototype.microservice.commons.error.CheckedException;

@Component
public class ExcelFileParser extends EtlFileParser {
//	public static final String SUFFIX_XLS=".xls";
//	public static final String SUFFIX_XLSX=".xlsx";

    private Workbook workbook;
    private Sheet sheet;
    private int currentSheetIndex = 0;


    @Override
    public void init() {
//		rptLogger = LoggerFactory
//				.getLogger(RptHelper.getLoggerName(configInfo.getFileNamePattern(), configInfo.getExecOnDate()));

        if (configInfo == null) {
            return;
        }
        if (configInfo.getColumns() == null || configInfo.getColumns().size() <= 0) {
            return;
        }
        try {
            if (in != null) {
                this.workbook = WorkbookFactory.create(in);
            }
        } catch (InvalidFormatException | IOException e1) {
            rptLogger.error(msgHelper.getMessage("RPT-ERR-004", new Object[]{"[ExcelFileParser]", (currentRowIndex + 1)}));
            rptLogger.error(e1.getMessage());
            e1.printStackTrace();
        }
        openSheet();
        if (workbook == null || sheet == null) {
            throw new RuntimeException("Reader must be open before it can be read.");
        }
    }

    private void openSheet() {
        if (workbook == null) {
            return;
        }
        this.sheet = workbook.getSheetAt(this.currentSheetIndex);
        if (rptLogger.isDebugEnabled()) {
            rptLogger.debug("Opening sheet " + sheet.getSheetName() + ".");
        }
        if (rptLogger.isDebugEnabled()) {
            rptLogger.debug("Openend sheet " + sheet.getSheetName() + ", with " + sheet.getPhysicalNumberOfRows() + " rows.");
        }
    }

    @Override
    public void execNativeSql(String tableName, List<ColumnMetaInfo> columnsInfo, List<String> values, Map<String, String> sysColValMap) {
        String sql = sqlAssembler.genNativeInsertSqlByColumnHeader(tableName, columnsInfo, values, sysColValMap, configInfo.getNullValFilter());
        System.out.println(sql);
        try {
            if (sql != null) {
                etlCommonRepository.execUpdate(sql);
            }
        } catch (Exception e) {
            String msg = msgHelper.getMessage("RPT-ERR-003", new Object[]{"[ExcelFileParser]", sql, e.getMessage()});
            throw new RuntimeException(msg);
        }
        //return sql;
    }

    public void setSheetIndex(int sheetIndex) {
        this.currentSheetIndex = sheetIndex;
    }

    @Override
    public List<Integer> getColumnIndices() {
        List<Integer> indices = new ArrayList<>();
        List<ColumnMetaInfo> columns = configInfo.getColumns();
        Row header = sheet.getRow(configInfo.getHeaderRowIndex());
        if (header != null && columns != null && header.getLastCellNum() > 0 && columns.size() > 0) {
            for (ColumnMetaInfo col : columns) {
                for (int i = 0; i < header.getLastCellNum(); i++) {
                    if (col.getFileColName().equalsIgnoreCase(header.getCell(i).getStringCellValue())) {
                        indices.add(i);
                    }
                }
            }
        } else if (columns == null && header != null) {
            header.forEach(c -> columnIndices.add(c.getColumnIndex()));
        }
        return indices;
    }

    @Override
    public List<String> parseColumnValues(Object r, List<ColumnMetaInfo> columns) throws Exception {
        List<String> valueStrList = new ArrayList<>();
        if (r instanceof Row) {
            Row row = (Row) r;
            for (int i = 0; i < row.getLastCellNum(); i++) {
                if (columnIndices.contains(i)) {
                    CellType cellType = row.getCell(i).getCellTypeEnum();
                    if (cellType == CellType.STRING) {
                        valueStrList.add(row.getCell(i).getStringCellValue());
                    } else if (HSSFDateUtil.isCellDateFormatted(row.getCell(i))) {
                        valueStrList.add(baseHelper.formatRawDate(row.getCell(i).getDateCellValue(), columns.get(i).getFormat()));
                    } else if (cellType == CellType.FORMULA) {
                        valueStrList.add(row.getCell(i).getCellFormula());
                    } else if (cellType == CellType.NUMERIC) {
                        valueStrList.add(String.valueOf(row.getCell(i).getNumericCellValue()));
                    }
                }
            }
        }
        valueStrList.replaceAll(String::trim);
        if (valueStrList.size() != columnIndices.size()) {
            throw new CheckedException(msgHelper.getMessage("RPT-ERR-002", new Object[]{""}));
        }
        return valueStrList;
    }

    @Override
    public boolean isBlankRow(Object r) {
        boolean isBlank = true;
        if (r instanceof Row) {
            Row row = (Row) r;
            for (Cell cell : row) {
                if (cell.getCellTypeEnum() != CellType.BLANK) {
                    isBlank = false;
                    break;
                }
            }
        }
        return isBlank;
    }

    @Override
    public boolean hasNextRow() {
        return currentRowIndex <= sheet.getPhysicalNumberOfRows() - configInfo.getEndRowIndexFromBottom();
    }

    @Override
    public Object getRow() {
        return sheet.getRow(currentRowIndex);
    }

    @Override
    public void clean() {
        etlCommonRepository.closeDbConn();
        currentRowIndex = 0;
        this.sheet = null;
        if (workbook != null) {
            try {
                workbook.close();
            } catch (IOException e) {
                rptLogger.error("Cannot close workbook");
                rptLogger.error(e.getMessage());
                e.printStackTrace();
            }
        }
        if (in != null) {
            try {
                in.reset();
                in.close();//go back to the first line
            } catch (IOException e) {
                rptLogger.error("Cannot close input stream.");
                rptLogger.error(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }

    @Override
    protected Charset getFileEncoding() {
        // TODO Auto-generated method stub
        return null;
    }
}
