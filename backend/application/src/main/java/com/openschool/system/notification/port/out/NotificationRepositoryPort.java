package com.openschool.system.notification.port.out;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.notification.Notification;
import com.openschool.domain.notification.NotificationStatus;
import com.openschool.domain.notification.NotificationType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository port for notification persistence operations
 */
public interface NotificationRepositoryPort {
    
    /**
     * Save a notification
     * 
     * @param notification the notification to save
     * @return the saved notification
     */
    Notification save(Notification notification);
    
    /**
     * Find a notification by ID
     * 
     * @param id the notification ID
     * @return the notification if found
     */
    Optional<Notification> findById(UUID id);
    
    /**
     * Find notifications for a user with pagination
     * 
     * @param userId the user ID
     * @param pageInfo pagination information
     * @return paginated notifications
     */
    PageResult<Notification> findByRecipientId(UUID userId, PageInfo pageInfo);
    
    /**
     * Find notifications by recipient and type
     * 
     * @param userId the user ID
     * @param type the notification type
     * @param pageInfo pagination information
     * @return paginated notifications
     */
    PageResult<Notification> findByRecipientIdAndType(UUID userId, NotificationType type, PageInfo pageInfo);
    
    /**
     * Find notifications by recipient and status
     * 
     * @param userId the user ID
     * @param status the notification status
     * @param pageInfo pagination information
     * @return paginated notifications
     */
    PageResult<Notification> findByRecipientIdAndStatus(UUID userId, NotificationStatus status, PageInfo pageInfo);
    
    /**
     * Find unread notifications for a user
     * 
     * @param userId the user ID
     * @param pageInfo pagination information
     * @return paginated unread notifications
     */
    PageResult<Notification> findUnreadByRecipientId(UUID userId, PageInfo pageInfo);
    
    /**
     * Find recent notifications for a user
     * 
     * @param userId the user ID
     * @param limit maximum number of notifications
     * @return list of recent notifications
     */
    List<Notification> findRecentByRecipientId(UUID userId, int limit);
    
    /**
     * Find notifications by category
     * 
     * @param userId the user ID
     * @param category the notification category
     * @param pageInfo pagination information
     * @return paginated notifications
     */
    PageResult<Notification> findByRecipientIdAndCategory(UUID userId, String category, PageInfo pageInfo);
    
    /**
     * Count unread notifications for a user
     * 
     * @param userId the user ID
     * @return unread count
     */
    long countUnreadByRecipientId(UUID userId);
    
    /**
     * Count unread notifications by type for a user
     * 
     * @param userId the user ID
     * @return map of type to count
     */
    Map<NotificationType, Long> countUnreadByRecipientIdGroupByType(UUID userId);
    
    /**
     * Count unread notifications by category for a user
     * 
     * @param userId the user ID
     * @return map of category to count
     */
    Map<String, Long> countUnreadByRecipientIdGroupByCategory(UUID userId);
    
    /**
     * Count unread notifications for a specific type
     * 
     * @param userId the user ID
     * @param type the notification type
     * @return unread count for the type
     */
    long countUnreadByRecipientIdAndType(UUID userId, NotificationType type);
    
    /**
     * Count unread notifications for a specific category
     * 
     * @param userId the user ID
     * @param category the notification category
     * @return unread count for the category
     */
    long countUnreadByRecipientIdAndCategory(UUID userId, String category);
    
    /**
     * Mark notification as read
     * 
     * @param notificationId the notification ID
     * @param userId the user ID (for security)
     * @return true if successfully marked as read
     */
    boolean markAsRead(UUID notificationId, UUID userId);
    
    /**
     * Mark multiple notifications as read
     * 
     * @param notificationIds list of notification IDs
     * @param userId the user ID (for security)
     * @return number of notifications marked as read
     */
    int markMultipleAsRead(List<UUID> notificationIds, UUID userId);
    
    /**
     * Mark all notifications as read for a user
     * 
     * @param userId the user ID
     * @return number of notifications marked as read
     */
    int markAllAsRead(UUID userId);
    
    /**
     * Mark all notifications in a category as read for a user
     * 
     * @param userId the user ID
     * @param category the notification category
     * @return number of notifications marked as read
     */
    int markAllAsReadByCategory(UUID userId, String category);
    
    /**
     * Find failed notifications that can be retried
     * 
     * @param before retry before this time
     * @return list of notifications to retry
     */
    List<Notification> findFailedNotificationsForRetry(LocalDateTime before);
    
    /**
     * Update notification status
     * 
     * @param notificationId the notification ID
     * @param status the new status
     * @return true if successfully updated
     */
    boolean updateStatus(UUID notificationId, NotificationStatus status);
    
    /**
     * Delete old notifications
     * 
     * @param before delete notifications created before this time
     * @return number of deleted notifications
     */
    int deleteOldNotifications(LocalDateTime before);
}
