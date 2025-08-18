package com.openschool.common.excel.port;

import com.openschool.domain.common.excel.ExcelColumn;

import java.util.List;

/**
 * Port interface for writing Excel files (Pure Java - no framework dependencies)
 */
public interface ExcelWriterPort {
    
    /**
     * Generate Excel file from data
     */
    byte[] generateExcelFile(List<?> data, List<ExcelColumn> columnDefinitions, 
                           String sheetName, boolean includeHeader);
    
    /**
     * Generate Excel template with headers only
     */
    byte[] generateTemplate(List<ExcelColumn> columnDefinitions, String sheetName);
    
    /**
     * Get field value from object using reflection
     */
    Object getFieldValue(Object object, String fieldName);
    
    /**
     * Format value for Excel cell
     */
    String formatCellValue(Object value);
}
