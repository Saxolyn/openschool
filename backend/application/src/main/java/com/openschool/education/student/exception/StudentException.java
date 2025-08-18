package com.openschool.education.student.exception;

public class StudentException extends RuntimeException {
    public StudentException(String message) {
        super(message);
    }
    
    public StudentException(String message, Throwable cause) {
        super(message, cause);
    }
}
