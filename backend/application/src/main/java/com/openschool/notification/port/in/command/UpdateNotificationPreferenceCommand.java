package com.openschool.notification.port.in.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

/**
 * Command for updating user notification preferences
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class UpdateNotificationPreferenceCommand {
    
    /**
     * ID of the user whose preferences to update
     */
    private UUID userId;
    
    /**
     * Whether email notifications are enabled
     */
    private Boolean emailEnabled;
    
    /**
     * Whether internal message notifications are enabled
     */
    private Boolean internalMessageEnabled;
    
    /**
     * Whether push notifications are enabled
     */
    private Boolean pushNotificationEnabled;
    
    /**
     * Whether SMS notifications are enabled
     */
    private Boolean smsEnabled;
    
    /**
     * Email address to send notifications to
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
    private Boolean digestMode;
    
    /**
     * How often to send digest emails (DAILY, WEEKLY, etc.)
     */
    private String digestFrequency;
    
    /**
     * Time of day to send digest emails (HH:mm format)
     */
    private String digestTime;
    
    /**
     * Quiet hours start time (HH:mm format)
     */
    private String quietHoursStart;
    
    /**
     * Quiet hours end time (HH:mm format)
     */
    private String quietHoursEnd;
    
    /**
     * Category-specific notification preferences
     */
    private Map<String, Map<String, Boolean>> categoryPreferences;
    
    /**
     * Additional custom preferences
     */
    private Map<String, Object> customPreferences;
}
