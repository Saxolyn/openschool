package com.openschool.common.excel.port;

import com.openschool.domain.common.excel.ExcelColumn;
import com.openschool.domain.common.excel.ExcelData;

import java.util.List;

/**
 * Port interface for reading Excel files (Pure Java - no framework dependencies)
 */
public interface ExcelReaderPort {
    
    /**
     * Read Excel file data from byte array
     */
    ExcelData readExcelData(byte[] fileData, String fileName, String sheetName, 
                           int headerRowIndex, int dataStartRowIndex);
    
    /**
     * Validate Excel file structure against expected columns
     */
    void validateExcelStructure(byte[] fileData, String fileName, List<ExcelColumn> expectedColumns);
    
    /**
     * Get available sheet names from Excel file
     */
    List<String> getSheetNames(byte[] fileData, String fileName);
    
    /**
     * Check if file is valid Excel format
     */
    boolean isValidExcelFile(byte[] fileData, String fileName);
}
