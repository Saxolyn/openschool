package com.openschool.domain.notification;

/**
 * Enum representing the status of a notification
 */
public enum NotificationStatus {
    /**
     * Notification has been created but not yet sent
     */
    PENDING,
    
    /**
     * Notification is currently being processed/sent
     */
    SENDING,
    
    /**
     * Notification has been successfully sent
     */
    SENT,
    
    /**
     * Notification has been delivered to the recipient
     */
    DELIVERED,
    
    /**
     * Notification has been read by the recipient
     */
    READ,
    
    /**
     * Notification sending failed
     */
    FAILED,
    
    /**
     * Notification was cancelled before sending
     */
    CANCELLED
}
