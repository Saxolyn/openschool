package com.openschool.system.notification.exception;

/**
 * Exception messages for notification system
 */
public enum ExceptionMessage {
    NOTIFICATION_NOT_FOUND("Notification not found"),
    NOTIFICATION_ACCESS_DENIED("Access denied to notification"),
    EMAIL_TEMPLATE_NOT_FOUND("Email template not found"),
    EMAIL_TEMPLATE_NAME_EXISTS("Email template name already exists"),
    EMAIL_TEMPLATE_INACTIVE("Email template is inactive"),
    EMAIL_SENDING_FAILED("Failed to send email"),
    EMAIL_INVALID_ADDRESS("Invalid email address"),
    EMAIL_SERVICE_UNAVAILABLE("Email service is unavailable"),
    PUSH_NOTIFICATION_FAILED("Failed to send push notification"),
    PUSH_SERVICE_UNAVAILABLE("Push notification service is unavailable"),
    NOTIFICATION_PREFERENCE_NOT_FOUND("Notification preferences not found"),
    INVALID_NOTIFICATION_DATA("Invalid notification data provided"),
    INVALID_TEMPLATE_DATA("Invalid template data provided"),
    INVALID_PREFERENCE_DATA("Invalid preference data provided"),
    TEMPLATE_VARIABLE_MISSING("Required template variable is missing"),
    TEMPLATE_PROCESSING_ERROR("Error processing template"),
    RECIPIENT_NOT_FOUND("Notification recipient not found"),
    SENDER_NOT_FOUND("Notification sender not found"),
    NOTIFICATION_ALREADY_READ("Notification is already marked as read"),
    NOTIFICATION_CANNOT_RETRY("Notification cannot be retried"),
    BULK_OPERATION_FAILED("Bulk notification operation failed"),
    INVALID_NOTIFICATION_TYPE("Invalid notification type"),
    INVALID_NOTIFICATION_STATUS("Invalid notification status"),
    INVALID_NOTIFICATION_PRIORITY("Invalid notification priority");
    
    private final String message;
    
    ExceptionMessage(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
}
