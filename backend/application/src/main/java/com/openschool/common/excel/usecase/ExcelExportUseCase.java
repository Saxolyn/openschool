package com.openschool.common.excel.usecase;

import com.openschool.common.excel.command.ExportExcelCommand;
import com.openschool.common.excel.result.ExportResult;

/**
 * Use case interface for Excel export operations (Pure Java)
 */
public interface ExcelExportUseCase<T> {
    
    /**
     * Export data to Excel file
     */
    ExportResult exportToExcel(ExportExcelCommand<T> command);
    
    /**
     * Export all data to Excel file with default settings
     */
    ExportResult exportAllToExcel(String fileName);
    
    /**
     * Export filtered data to Excel file
     */
    ExportResult exportFilteredToExcel(String fileName, Object filterCriteria);
}
