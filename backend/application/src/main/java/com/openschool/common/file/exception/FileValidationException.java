package com.openschool.common.file.exception;

import java.util.List;

/**
 * Exception thrown when file validation fails
 */
public class FileValidationException extends FileProcessingException {
    
    private final List<String> validationErrors;
    
    public FileValidationException(String message, List<String> validationErrors) {
        super(message);
        this.validationErrors = validationErrors;
    }
    
    public FileValidationException(String message, List<String> validationErrors, Throwable cause) {
        super(message, cause);
        this.validationErrors = validationErrors;
    }
    
    public List<String> getValidationErrors() {
        return validationErrors;
    }
}
