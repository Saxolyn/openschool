package com.openschool.system.notification.port.in;

import com.openschool.domain.notification.NotificationPreference;
import com.openschool.system.notification.port.in.command.UpdateNotificationPreferenceCommand;

import java.util.UUID;

/**
 * Use case for managing notification preferences
 */
public interface UpdateNotificationPreferenceUseCase {
    
    /**
     * Update notification preferences for a user
     * 
     * @param command the command containing preference updates
     * @return the updated notification preference
     */
    NotificationPreference updateNotificationPreference(UpdateNotificationPreferenceCommand command);
    
    /**
     * Get notification preferences for a user
     * 
     * @param userId the user ID
     * @return the user's notification preferences
     */
    NotificationPreference getNotificationPreference(UUID userId);
    
    /**
     * Create default notification preferences for a new user
     * 
     * @param userId the user ID
     * @return the created default notification preferences
     */
    NotificationPreference createDefaultPreferences(UUID userId);
}
