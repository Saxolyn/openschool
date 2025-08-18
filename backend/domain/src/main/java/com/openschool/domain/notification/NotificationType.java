package com.openschool.domain.notification;

/**
 * Enum representing different types of notifications in the system
 */
public enum NotificationType {
    /**
     * Email notification sent via email service
     */
    EMAIL,
    
    /**
     * Internal message displayed within the application
     */
    INTERNAL_MESSAGE,
    
    /**
     * Push notification for mobile/web browsers
     */
    PUSH_NOTIFICATION,
    
    /**
     * SMS notification (for future implementation)
     */
    SMS
}
