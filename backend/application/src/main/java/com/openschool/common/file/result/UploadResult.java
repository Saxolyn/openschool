package com.openschool.common.file.result;

import com.openschool.domain.common.file.FileMetadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Result object for file upload operations (Pure Java)
 */
@Getter
@Builder
@AllArgsConstructor
public class UploadResult {
    
    private final FileMetadata fileMetadata;
    private final boolean successful;
    private final String errorMessage;
    private final LocalDateTime uploadedAt;
    private final long processingTimeMs;
    private final String downloadUrl;
    private final String thumbnailUrl;
    
    public static UploadResult success(FileMetadata fileMetadata, long processingTimeMs) {
        return UploadResult.builder()
                .fileMetadata(fileMetadata)
                .successful(true)
                .uploadedAt(LocalDateTime.now())
                .processingTimeMs(processingTimeMs)
                .build();
    }
    
    public static UploadResult success(FileMetadata fileMetadata, String downloadUrl, 
                                     String thumbnailUrl, long processingTimeMs) {
        return UploadResult.builder()
                .fileMetadata(fileMetadata)
                .successful(true)
                .downloadUrl(downloadUrl)
                .thumbnailUrl(thumbnailUrl)
                .uploadedAt(LocalDateTime.now())
                .processingTimeMs(processingTimeMs)
                .build();
    }
    
    public static UploadResult failure(String errorMessage) {
        return UploadResult.builder()
                .successful(false)
                .errorMessage(errorMessage)
                .uploadedAt(LocalDateTime.now())
                .build();
    }
}
