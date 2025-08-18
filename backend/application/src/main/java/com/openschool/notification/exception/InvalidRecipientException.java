package com.openschool.notification.exception;

/**
 * Exception thrown when notification recipient is invalid
 */
public class InvalidRecipientException extends NotificationException {
    
    public InvalidRecipientException(String message) {
        super(message);
    }
    
    public InvalidRecipientException(String message, Throwable cause) {
        super(message, cause);
    }
}
