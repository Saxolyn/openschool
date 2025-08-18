package com.openschool.common.excel.command;

import com.openschool.domain.common.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * Command for Excel import operations (Pure Java - no framework dependencies)
 */
@Getter
@Builder
@AllArgsConstructor
public class ImportExcelCommand {
    
    private final byte[] fileData;
    private final String fileName;
    private final String sheetName;
    private final int headerRowIndex;
    private final int dataStartRowIndex;
    private final List<ExcelColumn> columnDefinitions;
    private final Map<String, Object> additionalParameters;
    private final boolean skipValidationErrors;
    private final int maxErrorCount;
    
    public ImportExcelCommand(byte[] fileData, String fileName, List<ExcelColumn> columnDefinitions) {
        this.fileData = fileData;
        this.fileName = fileName;
        this.columnDefinitions = columnDefinitions;
        this.sheetName = null;
        this.headerRowIndex = 0;
        this.dataStartRowIndex = 1;
        this.additionalParameters = null;
        this.skipValidationErrors = false;
        this.maxErrorCount = 100;
    }
}
