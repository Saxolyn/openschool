package com.openschool.domain.common.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain value object representing file metadata
 */
@Getter
@Builder
@AllArgsConstructor
public class FileMetadata {
    
    private final UUID id;
    private final String originalFileName;
    private final String storedFileName;
    private final String contentType;
    private final long fileSize;
    private final String filePath;
    private final String fileExtension;
    private final String checksum;
    private final FileCategory category;
    private final FileStatus status;
    private final UUID uploadedBy;
    private final LocalDateTime uploadedAt;
    private final LocalDateTime lastAccessedAt;
    private final String description;
    private final boolean isPublic;
    
    public boolean isImage() {
        return contentType != null && contentType.startsWith("image/");
    }
    
    public boolean isDocument() {
        return contentType != null && (
            contentType.equals("application/pdf") ||
            contentType.startsWith("application/vnd.openxmlformats") ||
            contentType.startsWith("application/msword")
        );
    }
    
    public boolean isVideo() {
        return contentType != null && contentType.startsWith("video/");
    }
    
    public boolean isAudio() {
        return contentType != null && contentType.startsWith("audio/");
    }
    
    public String getFormattedFileSize() {
        if (fileSize < 1024) {
            return fileSize + " B";
        } else if (fileSize < 1024 * 1024) {
            return String.format("%.1f KB", fileSize / 1024.0);
        } else if (fileSize < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", fileSize / (1024.0 * 1024.0));
        } else {
            return String.format("%.1f GB", fileSize / (1024.0 * 1024.0 * 1024.0));
        }
    }
}
