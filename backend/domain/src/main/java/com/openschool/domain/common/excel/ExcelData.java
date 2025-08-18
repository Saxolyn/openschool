package com.openschool.domain.common.excel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * Domain value object representing Excel file data
 */
@Getter
@Builder
@AllArgsConstructor
public class ExcelData {
    
    private final String fileName;
    private final String sheetName;
    private final List<String> headers;
    private final List<Map<String, Object>> rows;
    private final int totalRows;
    
    public boolean isEmpty() {
        return rows == null || rows.isEmpty();
    }
    
    public boolean hasHeaders() {
        return headers != null && !headers.isEmpty();
    }
}
