package com.openschool.education.schoolclass.exception;

public class SchoolClassException extends RuntimeException {
    public SchoolClassException(String message) {
        super(message);
    }
    
    public SchoolClassException(String message, Throwable cause) {
        super(message, cause);
    }
}
