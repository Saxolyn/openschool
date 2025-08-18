package com.openschool.common.file.exception;

/**
 * Exception thrown when file access is denied
 */
public class FileAccessDeniedException extends FileProcessingException {
    
    public FileAccessDeniedException(String message) {
        super(message);
    }
    
    public FileAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}
