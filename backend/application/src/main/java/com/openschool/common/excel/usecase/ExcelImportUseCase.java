package com.openschool.common.excel.usecase;

import com.openschool.common.excel.command.ImportExcelCommand;
import com.openschool.common.excel.result.ImportResult;

/**
 * Use case interface for Excel import operations (Pure Java)
 */
public interface ExcelImportUseCase<T> {
    
    /**
     * Import data from Excel file
     */
    ImportResult<T> importFromExcel(ImportExcelCommand command);
    
    /**
     * Generate import template for download
     */
    byte[] generateImportTemplate();
    
    /**
     * Validate Excel file before processing
     */
    void validateImportFile(ImportExcelCommand command);
}
