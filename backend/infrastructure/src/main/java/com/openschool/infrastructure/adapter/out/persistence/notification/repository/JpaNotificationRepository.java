package com.openschool.infrastructure.adapter.out.persistence.notification.repository;

import com.openschool.domain.notification.NotificationStatus;
import com.openschool.domain.notification.NotificationType;
import com.openschool.infrastructure.adapter.out.persistence.notification.entity.NotificationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * JPA repository for notifications
 */
@Repository
public interface JpaNotificationRepository extends JpaRepository<NotificationEntity, UUID> {
    
    /**
     * Find notifications by recipient ID with pagination
     */
    Page<NotificationEntity> findByRecipientIdOrderByCreatedAtDesc(UUID recipientId, Pageable pageable);
    
    /**
     * Find notifications by recipient and type
     */
    Page<NotificationEntity> findByRecipientIdAndTypeOrderByCreatedAtDesc(UUID recipientId, NotificationType type, Pageable pageable);
    
    /**
     * Find notifications by recipient and status
     */
    Page<NotificationEntity> findByRecipientIdAndStatusOrderByCreatedAtDesc(UUID recipientId, NotificationStatus status, Pageable pageable);
    
    /**
     * Find unread notifications by recipient
     */
    @Query("SELECT n FROM NotificationEntity n WHERE n.recipientId = :recipientId AND n.status != 'READ' ORDER BY n.createdAt DESC")
    Page<NotificationEntity> findUnreadByRecipientId(@Param("recipientId") UUID recipientId, Pageable pageable);
    
    /**
     * Find recent notifications by recipient
     */
    List<NotificationEntity> findTop10ByRecipientIdOrderByCreatedAtDesc(UUID recipientId);
    
    /**
     * Find notifications by category using JSON path
     */
    @Query("SELECT n FROM NotificationEntity n WHERE n.recipientId = :recipientId AND JSON_EXTRACT(n.metadata, '$.category') = :category ORDER BY n.createdAt DESC")
    Page<NotificationEntity> findByRecipientIdAndCategory(@Param("recipientId") UUID recipientId, @Param("category") String category, Pageable pageable);
    
    /**
     * Count unread notifications by recipient
     */
    @Query("SELECT COUNT(n) FROM NotificationEntity n WHERE n.recipientId = :recipientId AND n.status != 'READ'")
    long countUnreadByRecipientId(@Param("recipientId") UUID recipientId);
    
    /**
     * Count unread notifications by type
     */
    @Query("SELECT COUNT(n) FROM NotificationEntity n WHERE n.recipientId = :recipientId AND n.type = :type AND n.status != 'read'")
    long countUnreadByRecipientIdAndType(@Param("recipientId") UUID recipientId, @Param("type") NotificationType type);
    
    /**
     * Count unread notifications by category
     */
    @Query("SELECT COUNT(n) FROM NotificationEntity n WHERE n.recipientId = :recipientId AND JSON_EXTRACT(n.metadata, '$.category') = :category AND n.status != 'read'")
    long countUnreadByRecipientIdAndCategory(@Param("recipientId") UUID recipientId, @Param("category") String category);
    
    /**
     * Mark notification as read
     */
    @Modifying
    @Query("UPDATE NotificationEntity n SET n.status = 'READ', n.readAt = :readAt, n.updatedAt = :updatedAt WHERE n.id = :notificationId AND n.recipientId = :userId")
    int markAsRead(@Param("notificationId") UUID notificationId, @Param("userId") UUID userId, 
                   @Param("readAt") LocalDateTime readAt, @Param("updatedAt") LocalDateTime updatedAt);
    
    /**
     * Mark multiple notifications as read
     */
    @Modifying
    @Query("UPDATE NotificationEntity n SET n.status = 'READ', n.readAt = :readAt, n.updatedAt = :updatedAt WHERE n.id IN :notificationIds AND n.recipientId = :userId")
    int markMultipleAsRead(@Param("notificationIds") List<UUID> notificationIds, @Param("userId") UUID userId,
                          @Param("readAt") LocalDateTime readAt, @Param("updatedAt") LocalDateTime updatedAt);
    
    /**
     * Mark all notifications as read for a user
     */
    @Modifying
    @Query("UPDATE NotificationEntity n SET n.status = 'READ', n.readAt = :readAt, n.updatedAt = :updatedAt WHERE n.recipientId = :userId AND n.status != 'read'")
    int markAllAsRead(@Param("userId") UUID userId, @Param("readAt") LocalDateTime readAt, @Param("updatedAt") LocalDateTime updatedAt);
    
    /**
     * Mark all notifications in a category as read
     */
    @Modifying
    @Query("UPDATE NotificationEntity n SET n.status = 'READ', n.readAt = :readAt, n.updatedAt = :updatedAt WHERE n.recipientId = :userId AND JSON_EXTRACT(n.metadata, '$.category') = :category AND n.status != 'read'")
    int markAllAsReadByCategory(@Param("userId") UUID userId, @Param("category") String category,
                               @Param("readAt") LocalDateTime readAt, @Param("updatedAt") LocalDateTime updatedAt);
    
    /**
     * Find failed notifications for retry
     */
    @Query("SELECT n FROM NotificationEntity n WHERE n.status = 'FAILED' AND n.retryAt <= :before AND n.retryCount < n.maxRetries")
    List<NotificationEntity> findFailedNotificationsForRetry(@Param("before") LocalDateTime before);
    
    /**
     * Update notification status
     */
    @Modifying
    @Query("UPDATE NotificationEntity n SET n.status = :status, n.updatedAt = :updatedAt WHERE n.id = :notificationId")
    int updateStatus(@Param("notificationId") UUID notificationId, @Param("status") NotificationStatus status, @Param("updatedAt") LocalDateTime updatedAt);
    
    /**
     * Delete old notifications
     */
    @Modifying
    @Query("DELETE FROM NotificationEntity n WHERE n.createdAt < :before")
    int deleteByCreatedAtBefore(@Param("before") LocalDateTime before);
    
    /**
     * Get unread count grouped by type - native query for complex aggregation
     */
    @Query(value = "SELECT type, COUNT(*) as count FROM notifications WHERE recipient_id = :recipientId AND status != 'READ' GROUP BY type", nativeQuery = true)
    List<Object[]> countUnreadByRecipientIdGroupByType(@Param("recipientId") UUID recipientId);
    
    /**
     * Get unread count grouped by category - native query for JSON extraction
     */
    @Query(value = "SELECT metadata->>'category' as category, COUNT(*) as count FROM notifications WHERE recipient_id = :recipientId AND status != 'read' AND metadata->>'category' IS NOT NULL GROUP BY metadata->>'category'", nativeQuery = true)
    List<Object[]> countUnreadByRecipientIdGroupByCategory(@Param("recipientId") UUID recipientId);
}
