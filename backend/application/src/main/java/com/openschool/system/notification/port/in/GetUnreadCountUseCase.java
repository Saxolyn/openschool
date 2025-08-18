package com.openschool.system.notification.port.in;

import com.openschool.domain.notification.NotificationType;

import java.util.Map;
import java.util.UUID;

/**
 * Use case for getting unread notification counts
 */
public interface GetUnreadCountUseCase {
    
    /**
     * Get total unread notification count for a user
     * 
     * @param userId the user ID
     * @return total unread count
     */
    long getUnreadCount(UUID userId);
    
    /**
     * Get unread notification count by type for a user
     * 
     * @param userId the user ID
     * @return map of notification type to unread count
     */
    Map<NotificationType, Long> getUnreadCountByType(UUID userId);
    
    /**
     * Get unread notification count by category for a user
     * 
     * @param userId the user ID
     * @return map of category to unread count
     */
    Map<String, Long> getUnreadCountByCategory(UUID userId);
    
    /**
     * Get unread count for a specific notification type
     * 
     * @param userId the user ID
     * @param type the notification type
     * @return unread count for the specified type
     */
    long getUnreadCountForType(UUID userId, NotificationType type);
    
    /**
     * Get unread count for a specific category
     * 
     * @param userId the user ID
     * @param category the notification category
     * @return unread count for the specified category
     */
    long getUnreadCountForCategory(UUID userId, String category);
}
