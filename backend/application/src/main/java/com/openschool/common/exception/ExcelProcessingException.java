package com.openschool.common.exception;

/**
 * Base exception for Excel processing operations
 */
public class ExcelProcessingException extends RuntimeException {
    
    public ExcelProcessingException(String message) {
        super(message);
    }
    
    public ExcelProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
