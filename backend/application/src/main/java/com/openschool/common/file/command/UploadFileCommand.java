package com.openschool.common.file.command;

import com.openschool.domain.common.file.FileCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

/**
 * Command for file upload operations (Pure Java)
 */
@Getter
@Builder
@AllArgsConstructor
public class UploadFileCommand {
    
    private final byte[] fileData;
    private final String originalFileName;
    private final String contentType;
    private final FileCategory category;
    private final UUID uploadedBy;
    private final String description;
    private final boolean isPublic;
    private final Map<String, Object> additionalMetadata;
    private final boolean validateFileType;
    private final boolean generateThumbnail;
    private final long maxFileSize;
    
    public UploadFileCommand(byte[] fileData, String originalFileName, String contentType, 
                           FileCategory category, UUID uploadedBy) {
        this.fileData = fileData;
        this.originalFileName = originalFileName;
        this.contentType = contentType;
        this.category = category;
        this.uploadedBy = uploadedBy;
        this.description = null;
        this.isPublic = false;
        this.additionalMetadata = null;
        this.validateFileType = true;
        this.generateThumbnail = false;
        this.maxFileSize = 10 * 1024 * 1024; // 10MB default
    }
}
