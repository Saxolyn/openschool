package com.openschool.common.excel.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Result object for Excel export operations (Pure Java)
 */
@Getter
@Builder
@AllArgsConstructor
public class ExportResult {
    
    private final String fileName;
    private final byte[] fileContent;
    private final String contentType;
    private final long fileSize;
    private final int recordCount;
    private final LocalDateTime generatedAt;
    private final long processingTimeMs;
    private final boolean successful;
    private final String errorMessage;
    
    public static ExportResult success(String fileName, byte[] fileContent, int recordCount, long processingTimeMs) {
        return ExportResult.builder()
                .fileName(fileName)
                .fileContent(fileContent)
                .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .fileSize(fileContent.length)
                .recordCount(recordCount)
                .generatedAt(LocalDateTime.now())
                .processingTimeMs(processingTimeMs)
                .successful(true)
                .build();
    }
    
    public static ExportResult failure(String fileName, String errorMessage) {
        return ExportResult.builder()
                .fileName(fileName)
                .errorMessage(errorMessage)
                .generatedAt(LocalDateTime.now())
                .successful(false)
                .build();
    }
}
