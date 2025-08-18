package com.openschool.notification.exception;

/**
 * Exception thrown when email sending fails
 */
public class EmailSendingException extends NotificationException {
    
    public EmailSendingException(String message) {
        super(message);
    }
    
    public EmailSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
