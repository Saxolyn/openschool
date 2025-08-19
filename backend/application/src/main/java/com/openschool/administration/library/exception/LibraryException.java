package com.openschool.administration.library.exception;

import com.openschool.common.exception.ExceptionMessage;

public class LibraryException extends RuntimeException {
    
    public LibraryException(ExceptionMessage message) {
        super(message.getMessage());
    }
    
    public LibraryException(String message) {
        super(message);
    }
    
    public LibraryException(String message, Throwable cause) {
        super(message, cause);
    }
}
