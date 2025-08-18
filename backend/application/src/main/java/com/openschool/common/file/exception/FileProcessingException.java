package com.openschool.common.file.exception;

/**
 * Base exception for file processing operations
 */
public class FileProcessingException extends RuntimeException {
    
    public FileProcessingException(String message) {
        super(message);
    }
    
    public FileProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
