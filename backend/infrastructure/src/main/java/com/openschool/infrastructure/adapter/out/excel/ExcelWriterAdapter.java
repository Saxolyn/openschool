package com.openschool.infrastructure.adapter.out.excel;

import com.openschool.common.excel.port.ExcelWriterPort;
import com.openschool.common.exception.InvalidExcelFormatException;
import com.openschool.domain.common.excel.ExcelColumn;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Apache POI implementation of ExcelWriterPort
 */
@Component
public class ExcelWriterAdapter implements ExcelWriterPort {
    
    @Override
    public byte[] generateExcelFile(List<?> data, List<ExcelColumn> columnDefinitions, 
                                   String sheetName, boolean includeHeader) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(sheetName);
            
            int rowIndex = 0;
            
            // Create header row
            if (includeHeader) {
                Row headerRow = sheet.createRow(rowIndex++);
                for (int i = 0; i < columnDefinitions.size(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columnDefinitions.get(i).getName());
                    
                    // Apply header styling
                    CellStyle headerStyle = workbook.createCellStyle();
                    Font headerFont = workbook.createFont();
                    headerFont.setBold(true);
                    headerStyle.setFont(headerFont);
                    cell.setCellStyle(headerStyle);
                }
            }
            
            // Create data rows
            for (Object item : data) {
                Row row = sheet.createRow(rowIndex++);
                for (int i = 0; i < columnDefinitions.size(); i++) {
                    Cell cell = row.createCell(i);
                    ExcelColumn column = columnDefinitions.get(i);
                    
                    try {
                        Object value = getFieldValue(item, column.getFieldName());
                        setCellValue(cell, value);
                    } catch (Exception e) {
                        cell.setCellValue("Error: " + e.getMessage());
                    }
                }
            }
            
            // Auto-size columns
            for (int i = 0; i < columnDefinitions.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Convert to byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
            
        } catch (IOException e) {
            throw new InvalidExcelFormatException("Failed to generate Excel file: " + e.getMessage(), e);
        }
    }
    
    @Override
    public byte[] generateTemplate(List<ExcelColumn> columnDefinitions, String sheetName) {
        return generateExcelFile(Collections.emptyList(), columnDefinitions, sheetName, true);
    }
    
    @Override
    public Object getFieldValue(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get field value: " + fieldName, e);
        }
    }
    
    @Override
    public String formatCellValue(Object value) {
        if (value == null) {
            return "";
        }
        
        if (value instanceof LocalDate) {
            return ((LocalDate) value).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        
        if (value instanceof LocalDateTime) {
            return ((LocalDateTime) value).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        
        if (value instanceof Boolean) {
            return ((Boolean) value) ? "Yes" : "No";
        }
        
        return value.toString();
    }
    
    // Helper method
    private void setCellValue(Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
            return;
        }
        
        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else {
            cell.setCellValue(formatCellValue(value));
        }
    }
}
