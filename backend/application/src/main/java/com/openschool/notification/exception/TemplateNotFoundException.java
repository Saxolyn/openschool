package com.openschool.notification.exception;

/**
 * Exception thrown when email template is not found
 */
public class TemplateNotFoundException extends NotificationException {
    
    public TemplateNotFoundException(String message) {
        super(message);
    }
    
    public TemplateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
