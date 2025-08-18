package com.openschool.common.excel.command;

import com.openschool.domain.common.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * Command for Excel export operations (Pure Java - no framework dependencies)
 */
@Getter
@Builder
@AllArgsConstructor
public class ExportExcelCommand<T> {
    
    private final List<T> data;
    private final String fileName;
    private final String sheetName;
    private final List<ExcelColumn> columnDefinitions;
    private final Map<String, Object> additionalParameters;
    private final boolean includeHeader;
    private final String templatePath;
    
    public ExportExcelCommand(List<T> data, String fileName, List<ExcelColumn> columnDefinitions) {
        this.data = data;
        this.fileName = fileName;
        this.columnDefinitions = columnDefinitions;
        this.sheetName = "Sheet1";
        this.includeHeader = true;
        this.templatePath = null;
        this.additionalParameters = null;
    }
}
