package com.openschool.infrastructure.adapter.out.excel;

import com.openschool.common.excel.port.ExcelReaderPort;
import com.openschool.common.excel.validation.ExcelDataValidator;
import com.openschool.common.exception.InvalidExcelFormatException;
import com.openschool.domain.common.excel.ExcelColumn;
import com.openschool.domain.common.excel.ExcelData;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Apache POI implementation of ExcelReaderPort
 */
@Component
public class ExcelReaderAdapter implements ExcelReaderPort {
    
    @Override
    public ExcelData readExcelData(byte[] fileData, String fileName, String sheetName, 
                                  int headerRowIndex, int dataStartRowIndex) {
        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(fileData))) {
            Sheet sheet = getSheet(workbook, sheetName);
            
            // Read headers
            Row headerRow = sheet.getRow(headerRowIndex);
            if (headerRow == null) {
                throw new InvalidExcelFormatException("Header row not found at index " + headerRowIndex);
            }
            
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(getCellValueAsString(cell));
            }
            
            // Read data rows
            List<Map<String, Object>> rows = new ArrayList<>();
            for (int i = dataStartRowIndex; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isEmptyRow(row)) {
                    continue;
                }
                
                Map<String, Object> rowData = new HashMap<>();
                for (int j = 0; j < headers.size() && j < row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);
                    Object value = getCellValue(cell);
                    rowData.put(headers.get(j), value);
                }
                rows.add(rowData);
            }
            
            return ExcelData.builder()
                    .fileName(fileName)
                    .sheetName(sheet.getSheetName())
                    .headers(headers)
                    .rows(rows)
                    .totalRows(rows.size())
                    .build();
            
        } catch (IOException e) {
            throw new InvalidExcelFormatException("Failed to read Excel file: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void validateExcelStructure(byte[] fileData, String fileName, List<ExcelColumn> expectedColumns) {
        if (fileData == null || fileData.length == 0) {
            throw new InvalidExcelFormatException("File is empty or null");
        }
        
        if (!isValidExcelFile(fileData, fileName)) {
            throw new InvalidExcelFormatException("File must be an Excel file (.xlsx or .xls)");
        }
        
        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(fileData))) {
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            
            if (headerRow == null) {
                throw new InvalidExcelFormatException("Excel file must have a header row");
            }
            
            List<String> actualColumns = new ArrayList<>();
            for (Cell cell : headerRow) {
                actualColumns.add(getCellValueAsString(cell));
            }
            
            ExcelDataValidator.validateRequiredColumns(actualColumns, expectedColumns);
            
        } catch (IOException e) {
            throw new InvalidExcelFormatException("Failed to validate Excel file: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<String> getSheetNames(byte[] fileData, String fileName) {
        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(fileData))) {
            List<String> sheetNames = new ArrayList<>();
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                sheetNames.add(workbook.getSheetName(i));
            }
            return sheetNames;
        } catch (IOException e) {
            throw new InvalidExcelFormatException("Failed to read sheet names: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean isValidExcelFile(byte[] fileData, String fileName) {
        if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
            return false;
        }
        
        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(fileData))) {
            return workbook.getNumberOfSheets() > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    // Helper methods
    private Sheet getSheet(Workbook workbook, String sheetName) {
        if (sheetName != null && !sheetName.isEmpty()) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet != null) {
                return sheet;
            }
        }
        return workbook.getSheetAt(0);
    }
    
    private Object getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }
    
    private String getCellValueAsString(Cell cell) {
        Object value = getCellValue(cell);
        return value != null ? value.toString().trim() : "";
    }
    
    private boolean isEmptyRow(Row row) {
        for (Cell cell : row) {
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                String value = getCellValueAsString(cell);
                if (!value.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }
}
