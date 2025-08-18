package com.openschool.domain.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Domain entity representing a notification in the system
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Notification {
    
    /**
     * Unique identifier for the notification
     */
    private UUID id;
    
    /**
     * ID of the user who will receive the notification
     */
    private UUID recipientId;
    
    /**
     * ID of the user who sent the notification (optional for system notifications)
     */
    private UUID senderId;
    
    /**
     * Title/subject of the notification
     */
    private String title;
    
    /**
     * Main content/body of the notification
     */
    private String content;
    
    /**
     * Type of notification (EMAIL, INTERNAL_MESSAGE, etc.)
     */
    private NotificationType type;
    
    /**
     * Current status of the notification
     */
    private NotificationStatus status;
    
    /**
     * Priority level of the notification
     */
    private NotificationPriority priority;
    
    /**
     * Email address for email notifications
     */
    private String email;
    
    /**
     * Template ID used for this notification (if any)
     */
    private UUID templateId;
    
    /**
     * Variables used to populate the template
     */
    private Map<String, Object> templateVariables;
    
    /**
     * Additional metadata for the notification
     */
    private Map<String, Object> metadata;
    
    /**
     * When the notification was created
     */
    private LocalDateTime createdAt;
    
    /**
     * When the notification was sent
     */
    private LocalDateTime sentAt;
    
    /**
     * When the notification was delivered
     */
    private LocalDateTime deliveredAt;
    
    /**
     * When the notification was read by the recipient
     */
    private LocalDateTime readAt;
    
    /**
     * When the notification was last updated
     */
    private LocalDateTime updatedAt;
    
    /**
     * Error message if the notification failed to send
     */
    private String errorMessage;
    
    /**
     * Number of retry attempts for failed notifications
     */
    private Integer retryCount;
    
    /**
     * Maximum number of retry attempts allowed
     */
    private Integer maxRetries;
    
    /**
     * When to retry sending the notification (for failed notifications)
     */
    private LocalDateTime retryAt;
    
    /**
     * Check if the notification has been read
     */
    public boolean isRead() {
        return status == NotificationStatus.READ;
    }
    
    /**
     * Check if the notification has been sent successfully
     */
    public boolean isSent() {
        return status == NotificationStatus.SENT || 
               status == NotificationStatus.DELIVERED || 
               status == NotificationStatus.READ;
    }
    
    /**
     * Check if the notification has failed
     */
    public boolean isFailed() {
        return status == NotificationStatus.FAILED;
    }
    
    /**
     * Check if the notification is pending
     */
    public boolean isPending() {
        return status == NotificationStatus.PENDING;
    }
    
    /**
     * Check if the notification can be retried
     */
    public boolean canRetry() {
        return isFailed() && 
               retryCount != null && 
               maxRetries != null && 
               retryCount < maxRetries;
    }
    
    /**
     * Mark the notification as read
     */
    public void markAsRead() {
        this.status = NotificationStatus.READ;
        this.readAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Mark the notification as sent
     */
    public void markAsSent() {
        this.status = NotificationStatus.SENT;
        this.sentAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Mark the notification as failed with error message
     */
    public void markAsFailed(String errorMessage) {
        this.status = NotificationStatus.FAILED;
        this.errorMessage = errorMessage;
        this.updatedAt = LocalDateTime.now();
        
        if (this.retryCount == null) {
            this.retryCount = 0;
        }
        this.retryCount++;
        
        // Set retry time (exponential backoff)
        if (canRetry()) {
            long delayMinutes = (long) Math.pow(2, retryCount) * 5; // 5, 10, 20, 40 minutes
            this.retryAt = LocalDateTime.now().plusMinutes(delayMinutes);
        }
    }
}
