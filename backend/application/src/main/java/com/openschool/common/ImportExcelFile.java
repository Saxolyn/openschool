package com.openschool.common;

import com.openschool.common.excel.command.ImportExcelCommand;
import com.openschool.common.excel.result.ImportResult;

/**
 * Legacy interface for Excel import operations (Pure Java)
 * @deprecated Use ExcelImportUseCase instead
 */
@Deprecated
public interface ImportExcelFile<T> {

    /**
     * Import data from Excel file
     */
    ImportResult<T> importFromExcel(ImportExcelCommand command);
}
