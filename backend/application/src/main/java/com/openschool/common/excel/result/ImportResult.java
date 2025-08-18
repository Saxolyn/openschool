package com.openschool.common.excel.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Result object for Excel import operations (Pure Java)
 */
@Getter
@Builder
@AllArgsConstructor
public class ImportResult<T> {
    
    private final List<T> successfulRecords;
    private final List<ImportError> errors;
    private final int totalRecords;
    private final int successCount;
    private final int errorCount;
    private final LocalDateTime processedAt;
    private final String fileName;
    private final long processingTimeMs;
    
    public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }
    
    public boolean isSuccessful() {
        return errorCount == 0;
    }
    
    public double getSuccessRate() {
        if (totalRecords == 0) return 0.0;
        return (double) successCount / totalRecords * 100;
    }
    
    @Getter
    @Builder
    @AllArgsConstructor
    public static class ImportError {
        private final int rowNumber;
        private final String columnName;
        private final String fieldName;
        private final String errorMessage;
        private final String cellValue;
    }
}
