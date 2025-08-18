package com.openschool.system.notification.port.in;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.notification.Notification;
import com.openschool.domain.notification.NotificationStatus;
import com.openschool.domain.notification.NotificationType;

import java.util.List;
import java.util.UUID;

/**
 * Use case for retrieving notifications
 */
public interface GetNotificationsUseCase {
    
    /**
     * Get notifications for a specific user with pagination
     * 
     * @param userId the user ID
     * @param pageInfo pagination information
     * @return paginated list of notifications
     */
    PageResult<Notification> getNotificationsForUser(UUID userId, PageInfo pageInfo);
    
    /**
     * Get notifications for a user filtered by type
     * 
     * @param userId the user ID
     * @param type the notification type
     * @param pageInfo pagination information
     * @return paginated list of notifications
     */
    PageResult<Notification> getNotificationsByType(UUID userId, NotificationType type, PageInfo pageInfo);
    
    /**
     * Get notifications for a user filtered by status
     * 
     * @param userId the user ID
     * @param status the notification status
     * @param pageInfo pagination information
     * @return paginated list of notifications
     */
    PageResult<Notification> getNotificationsByStatus(UUID userId, NotificationStatus status, PageInfo pageInfo);
    
    /**
     * Get unread notifications for a user
     * 
     * @param userId the user ID
     * @param pageInfo pagination information
     * @return paginated list of unread notifications
     */
    PageResult<Notification> getUnreadNotifications(UUID userId, PageInfo pageInfo);
    
    /**
     * Get recent notifications for a user (last N notifications)
     * 
     * @param userId the user ID
     * @param limit maximum number of notifications to return
     * @return list of recent notifications
     */
    List<Notification> getRecentNotifications(UUID userId, int limit);
    
    /**
     * Get notifications by category
     * 
     * @param userId the user ID
     * @param category the notification category
     * @param pageInfo pagination information
     * @return paginated list of notifications
     */
    PageResult<Notification> getNotificationsByCategory(UUID userId, String category, PageInfo pageInfo);
}
