package com.openschool.common.exception;

import java.util.List;

/**
 * Exception thrown when Excel data validation fails
 */
public class ExcelValidationException extends ExcelProcessingException {
    
    private final List<String> validationErrors;
    
    public ExcelValidationException(String message, List<String> validationErrors) {
        super(message);
        this.validationErrors = validationErrors;
    }
    
    public ExcelValidationException(String message, List<String> validationErrors, Throwable cause) {
        super(message, cause);
        this.validationErrors = validationErrors;
    }
    
    public List<String> getValidationErrors() {
        return validationErrors;
    }
}
