package com.openschool.common.file.result;

import com.openschool.domain.common.file.FileData;
import com.openschool.domain.common.file.FileMetadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Result object for file download operations (Pure Java)
 */
@Getter
@Builder
@AllArgsConstructor
public class DownloadResult {
    
    private final FileData fileData;
    private final boolean successful;
    private final String errorMessage;
    private final LocalDateTime accessedAt;
    private final long processingTimeMs;
    
    public static DownloadResult success(FileData fileData, long processingTimeMs) {
        return DownloadResult.builder()
                .fileData(fileData)
                .successful(true)
                .accessedAt(LocalDateTime.now())
                .processingTimeMs(processingTimeMs)
                .build();
    }
    
    public static DownloadResult failure(String errorMessage) {
        return DownloadResult.builder()
                .successful(false)
                .errorMessage(errorMessage)
                .accessedAt(LocalDateTime.now())
                .build();
    }
    
    public FileMetadata getMetadata() {
        return fileData != null ? fileData.getMetadata() : null;
    }
    
    public byte[] getContent() {
        return fileData != null ? fileData.getContent() : null;
    }
}
