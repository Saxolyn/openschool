package com.openschool.common.file.usecase;

import com.openschool.common.file.command.UploadFileCommand;
import com.openschool.common.file.result.UploadResult;

/**
 * Use case interface for file upload operations (Pure Java)
 */
public interface UploadFileUseCase {
    
    /**
     * Upload file
     */
    UploadResult uploadFile(UploadFileCommand command);
    
    /**
     * Validate file before upload
     */
    void validateFile(UploadFileCommand command);
    
    /**
     * Get maximum allowed file size for category
     */
    long getMaxFileSize(String category);
    
    /**
     * Get allowed file types for category
     */
    String[] getAllowedFileTypes(String category);
}
