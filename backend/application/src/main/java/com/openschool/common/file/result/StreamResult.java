package com.openschool.common.file.result;

import com.openschool.domain.common.file.FileMetadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.InputStream;
import java.time.LocalDateTime;

/**
 * Result object for file streaming operations (Pure Java)
 */
@Getter
@Builder
@AllArgsConstructor
public class StreamResult {
    
    private final InputStream inputStream;
    private final FileMetadata fileMetadata;
    private final long contentLength;
    private final long rangeStart;
    private final long rangeEnd;
    private final boolean isPartialContent;
    private final boolean successful;
    private final String errorMessage;
    private final LocalDateTime accessedAt;
    
    public static StreamResult success(InputStream inputStream, FileMetadata fileMetadata, 
                                     long contentLength) {
        return StreamResult.builder()
                .inputStream(inputStream)
                .fileMetadata(fileMetadata)
                .contentLength(contentLength)
                .rangeStart(0)
                .rangeEnd(contentLength - 1)
                .isPartialContent(false)
                .successful(true)
                .accessedAt(LocalDateTime.now())
                .build();
    }
    
    public static StreamResult successPartial(InputStream inputStream, FileMetadata fileMetadata,
                                            long contentLength, long rangeStart, long rangeEnd) {
        return StreamResult.builder()
                .inputStream(inputStream)
                .fileMetadata(fileMetadata)
                .contentLength(contentLength)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .isPartialContent(true)
                .successful(true)
                .accessedAt(LocalDateTime.now())
                .build();
    }
    
    public static StreamResult failure(String errorMessage) {
        return StreamResult.builder()
                .successful(false)
                .errorMessage(errorMessage)
                .accessedAt(LocalDateTime.now())
                .build();
    }
    
    public String getContentRange() {
        if (isPartialContent) {
            return String.format("bytes %d-%d/%d", rangeStart, rangeEnd, 
                fileMetadata != null ? fileMetadata.getFileSize() : contentLength);
        }
        return null;
    }
}
