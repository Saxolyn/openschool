package com.openschool.system.notification.port.out;

import com.openschool.domain.notification.NotificationPreference;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository port for notification preference persistence operations
 */
public interface NotificationPreferenceRepositoryPort {
    
    /**
     * Save notification preferences
     * 
     * @param preference the preferences to save
     * @return the saved preferences
     */
    NotificationPreference save(NotificationPreference preference);
    
    /**
     * Find preferences by user ID
     * 
     * @param userId the user ID
     * @return the user's preferences if found
     */
    Optional<NotificationPreference> findByUserId(UUID userId);
    
    /**
     * Check if preferences exist for a user
     * 
     * @param userId the user ID
     * @return true if preferences exist
     */
    boolean existsByUserId(UUID userId);
    
    /**
     * Delete preferences for a user
     * 
     * @param userId the user ID
     * @return true if successfully deleted
     */
    boolean deleteByUserId(UUID userId);
}
