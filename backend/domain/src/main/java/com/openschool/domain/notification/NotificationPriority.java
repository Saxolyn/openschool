package com.openschool.domain.notification;

/**
 * Enum representing the priority level of a notification
 */
public enum NotificationPriority {
    /**
     * Low priority notification - can be delayed
     */
    LOW,
    
    /**
     * Normal priority notification - default level
     */
    NORMAL,
    
    /**
     * High priority notification - should be sent immediately
     */
    HIGH,
    
    /**
     * Critical priority notification - urgent, must be sent immediately
     */
    CRITICAL
}
