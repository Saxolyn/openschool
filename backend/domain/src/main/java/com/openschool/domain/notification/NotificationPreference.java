package com.openschool.domain.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Domain entity representing user notification preferences
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class NotificationPreference {
    
    /**
     * Unique identifier for the preference record
     */
    private UUID id;
    
    /**
     * ID of the user these preferences belong to
     */
    private UUID userId;
    
    /**
     * Whether email notifications are enabled
     */
    private boolean emailEnabled;
    
    /**
     * Whether internal message notifications are enabled
     */
    private boolean internalMessageEnabled;
    
    /**
     * Whether push notifications are enabled
     */
    private boolean pushNotificationEnabled;
    
    /**
     * Whether SMS notifications are enabled
     */
    private boolean smsEnabled;
    
    /**
     * Email address to send notifications to (if different from user's primary email)
     */
    private String notificationEmail;
    
    /**
     * Phone number for SMS notifications
     */
    private String notificationPhone;
    
    /**
     * Preferred language for notifications
     */
    private String preferredLanguage;
    
    /**
     * Timezone for scheduling notifications
     */
    private String timezone;
    
    /**
     * Whether to send digest emails instead of individual notifications
     */
    private boolean digestMode;
    
    /**
     * How often to send digest emails (DAILY, WEEKLY, etc.)
     */
    private String digestFrequency;
    
    /**
     * Time of day to send digest emails (HH:mm format)
     */
    private String digestTime;
    
    /**
     * Quiet hours start time (HH:mm format) - no notifications during this period
     */
    private String quietHoursStart;
    
    /**
     * Quiet hours end time (HH:mm format)
     */
    private String quietHoursEnd;
    
    /**
     * Category-specific notification preferences
     * Key: category name (e.g., "ACADEMIC", "ADMINISTRATIVE", "EMERGENCY")
     * Value: Map of notification type to enabled status
     */
    private Map<String, Map<String, Boolean>> categoryPreferences;
    
    /**
     * Additional custom preferences
     */
    private Map<String, Object> customPreferences;
    
    /**
     * When the preferences were created
     */
    private LocalDateTime createdAt;
    
    /**
     * When the preferences were last updated
     */
    private LocalDateTime updatedAt;
    
    /**
     * Check if a specific notification type is enabled
     */
    public boolean isNotificationTypeEnabled(NotificationType type) {
        switch (type) {
            case EMAIL:
                return emailEnabled;
            case INTERNAL_MESSAGE:
                return internalMessageEnabled;
            case PUSH_NOTIFICATION:
                return pushNotificationEnabled;
            case SMS:
                return smsEnabled;
            default:
                return false;
        }
    }
    
    /**
     * Check if notifications are enabled for a specific category and type
     */
    public boolean isCategoryNotificationEnabled(String category, NotificationType type) {
        if (!isNotificationTypeEnabled(type)) {
            return false;
        }
        
        if (categoryPreferences == null || !categoryPreferences.containsKey(category)) {
            return true; // Default to enabled if no specific preference
        }
        
        Map<String, Boolean> categorySettings = categoryPreferences.get(category);
        String typeKey = type.name();
        
        return categorySettings.getOrDefault(typeKey, true);
    }
    
    /**
     * Enable a specific notification type
     */
    public void enableNotificationType(NotificationType type) {
        switch (type) {
            case EMAIL:
                this.emailEnabled = true;
                break;
            case INTERNAL_MESSAGE:
                this.internalMessageEnabled = true;
                break;
            case PUSH_NOTIFICATION:
                this.pushNotificationEnabled = true;
                break;
            case SMS:
                this.smsEnabled = true;
                break;
        }
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Disable a specific notification type
     */
    public void disableNotificationType(NotificationType type) {
        switch (type) {
            case EMAIL:
                this.emailEnabled = false;
                break;
            case INTERNAL_MESSAGE:
                this.internalMessageEnabled = false;
                break;
            case PUSH_NOTIFICATION:
                this.pushNotificationEnabled = false;
                break;
            case SMS:
                this.smsEnabled = false;
                break;
        }
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Check if currently in quiet hours
     */
    public boolean isInQuietHours() {
        if (quietHoursStart == null || quietHoursEnd == null) {
            return false;
        }
        
        // This is a simplified check - in real implementation, 
        // you'd want to consider timezone and handle cases where 
        // quiet hours span midnight
        LocalDateTime now = LocalDateTime.now();
        String currentTime = String.format("%02d:%02d", now.getHour(), now.getMinute());
        
        return currentTime.compareTo(quietHoursStart) >= 0 && 
               currentTime.compareTo(quietHoursEnd) <= 0;
    }
}
