package com.openschool.common.exception;

/**
 * Exception thrown when Excel file format is invalid or corrupted
 */
public class InvalidExcelFormatException extends ExcelProcessingException {
    
    public InvalidExcelFormatException(String message) {
        super(message);
    }
    
    public InvalidExcelFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
