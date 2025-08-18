package com.openschool.common.file.exception;

/**
 * Exception thrown when file is not found
 */
public class FileNotFoundException extends FileProcessingException {
    
    public FileNotFoundException(String message) {
        super(message);
    }
    
    public FileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
