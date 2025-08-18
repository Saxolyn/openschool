package com.openschool.infrastructure.adapter.out.persistence.notification.repository;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.notification.Notification;
import com.openschool.domain.notification.NotificationStatus;
import com.openschool.domain.notification.NotificationType;
import com.openschool.infrastructure.adapter.out.persistence.notification.entity.NotificationEntity;
import com.openschool.notification.port.out.NotificationRepositoryPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * JPA implementation of NotificationRepositoryPort
 */
@Slf4j
@Repository
@AllArgsConstructor
public class NotificationRepositoryAdapter implements NotificationRepositoryPort {
    
    private final JpaNotificationRepository jpaNotificationRepository;
    
    @Override
    public Notification save(Notification notification) {
        NotificationEntity entity = NotificationEntity.fromDomain(notification);
        NotificationEntity savedEntity = jpaNotificationRepository.save(entity);
        return savedEntity.toDomain();
    }
    
    @Override
    public Optional<Notification> findById(UUID id) {
        return jpaNotificationRepository.findById(id)
            .map(NotificationEntity::toDomain);
    }
    
    @Override
    public PageResult<Notification> findByRecipientId(UUID userId, PageInfo pageInfo) {
        Pageable pageable = PageRequest.of(pageInfo.getPage(), pageInfo.getSize());
        Page<NotificationEntity> page = jpaNotificationRepository.findByRecipientIdOrderByCreatedAtDesc(userId, pageable);
        
        List<Notification> notifications = page.getContent().stream()
            .map(NotificationEntity::toDomain)
            .collect(Collectors.toList());
        
        return new PageResult<>(notifications, page.getTotalElements(), page.getTotalPages(), page.getNumber());
    }
    
    @Override
    public PageResult<Notification> findByRecipientIdAndType(UUID userId, NotificationType type, PageInfo pageInfo) {
        Pageable pageable = PageRequest.of(pageInfo.getPage(), pageInfo.getSize());
        Page<NotificationEntity> page = jpaNotificationRepository.findByRecipientIdAndTypeOrderByCreatedAtDesc(userId, type, pageable);
        
        List<Notification> notifications = page.getContent().stream()
            .map(NotificationEntity::toDomain)
            .collect(Collectors.toList());
        
        return new PageResult<>(notifications, page.getTotalElements(), page.getTotalPages(), page.getNumber());
    }
    
    @Override
    public PageResult<Notification> findByRecipientIdAndStatus(UUID userId, NotificationStatus status, PageInfo pageInfo) {
        Pageable pageable = PageRequest.of(pageInfo.getPage(), pageInfo.getSize());
        Page<NotificationEntity> page = jpaNotificationRepository.findByRecipientIdAndStatusOrderByCreatedAtDesc(userId, status, pageable);
        
        List<Notification> notifications = page.getContent().stream()
            .map(NotificationEntity::toDomain)
            .collect(Collectors.toList());
        
        return new PageResult<>(notifications, page.getTotalElements(), page.getTotalPages(), page.getNumber());
    }
    
    @Override
    public PageResult<Notification> findUnreadByRecipientId(UUID userId, PageInfo pageInfo) {
        Pageable pageable = PageRequest.of(pageInfo.getPage(), pageInfo.getSize());
        Page<NotificationEntity> page = jpaNotificationRepository.findUnreadByRecipientId(userId, pageable);
        
        List<Notification> notifications = page.getContent().stream()
            .map(NotificationEntity::toDomain)
            .collect(Collectors.toList());
        
        return new PageResult<>(notifications, page.getTotalElements(), page.getTotalPages(), page.getNumber());
    }
    
    @Override
    public List<Notification> findRecentByRecipientId(UUID userId, int limit) {
        List<NotificationEntity> entities;
        if (limit <= 10) {
            entities = jpaNotificationRepository.findTop10ByRecipientIdOrderByCreatedAtDesc(userId);
            if (limit < 10) {
                entities = entities.stream().limit(limit).collect(Collectors.toList());
            }
        } else {
            Pageable pageable = PageRequest.of(0, limit);
            Page<NotificationEntity> page = jpaNotificationRepository.findByRecipientIdOrderByCreatedAtDesc(userId, pageable);
            entities = page.getContent();
        }
        
        return entities.stream()
            .map(NotificationEntity::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public PageResult<Notification> findByRecipientIdAndCategory(UUID userId, String category, PageInfo pageInfo) {
        Pageable pageable = PageRequest.of(pageInfo.getPage(), pageInfo.getSize());
        Page<NotificationEntity> page = jpaNotificationRepository.findByRecipientIdAndCategory(userId, category, pageable);
        
        List<Notification> notifications = page.getContent().stream()
            .map(NotificationEntity::toDomain)
            .collect(Collectors.toList());
        
        return new PageResult<>(notifications, page.getTotalElements(), page.getTotalPages(), page.getNumber());
    }
    
    @Override
    public long countUnreadByRecipientId(UUID userId) {
        return jpaNotificationRepository.countUnreadByRecipientId(userId);
    }
    
    @Override
    public Map<NotificationType, Long> countUnreadByRecipientIdGroupByType(UUID userId) {
        List<Object[]> results = jpaNotificationRepository.countUnreadByRecipientIdGroupByType(userId);
        Map<NotificationType, Long> counts = new HashMap<>();
        
        for (Object[] result : results) {
            String typeStr = (String) result[0];
            Long count = ((Number) result[1]).longValue();
            try {
                NotificationType type = NotificationType.valueOf(typeStr);
                counts.put(type, count);
            } catch (IllegalArgumentException e) {
                log.warn("Unknown notification type: {}", typeStr);
            }
        }
        
        return counts;
    }
    
    @Override
    public Map<String, Long> countUnreadByRecipientIdGroupByCategory(UUID userId) {
        List<Object[]> results = jpaNotificationRepository.countUnreadByRecipientIdGroupByCategory(userId);
        Map<String, Long> counts = new HashMap<>();
        
        for (Object[] result : results) {
            String category = (String) result[0];
            Long count = ((Number) result[1]).longValue();
            if (category != null) {
                counts.put(category, count);
            }
        }
        
        return counts;
    }
    
    @Override
    public long countUnreadByRecipientIdAndType(UUID userId, NotificationType type) {
        return jpaNotificationRepository.countUnreadByRecipientIdAndType(userId, type);
    }
    
    @Override
    public long countUnreadByRecipientIdAndCategory(UUID userId, String category) {
        return jpaNotificationRepository.countUnreadByRecipientIdAndCategory(userId, category);
    }
    
    @Override
    @Transactional
    public boolean markAsRead(UUID notificationId, UUID userId) {
        LocalDateTime now = LocalDateTime.now();
        int updated = jpaNotificationRepository.markAsRead(notificationId, userId, now, now);
        return updated > 0;
    }
    
    @Override
    @Transactional
    public int markMultipleAsRead(List<UUID> notificationIds, UUID userId) {
        LocalDateTime now = LocalDateTime.now();
        return jpaNotificationRepository.markMultipleAsRead(notificationIds, userId, now, now);
    }
    
    @Override
    @Transactional
    public int markAllAsRead(UUID userId) {
        LocalDateTime now = LocalDateTime.now();
        return jpaNotificationRepository.markAllAsRead(userId, now, now);
    }
    
    @Override
    @Transactional
    public int markAllAsReadByCategory(UUID userId, String category) {
        LocalDateTime now = LocalDateTime.now();
        return jpaNotificationRepository.markAllAsReadByCategory(userId, category, now, now);
    }
    
    @Override
    public List<Notification> findFailedNotificationsForRetry(LocalDateTime before) {
        List<NotificationEntity> entities = jpaNotificationRepository.findFailedNotificationsForRetry(before);
        return entities.stream()
            .map(NotificationEntity::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public boolean updateStatus(UUID notificationId, NotificationStatus status) {
        LocalDateTime now = LocalDateTime.now();
        int updated = jpaNotificationRepository.updateStatus(notificationId, status, now);
        return updated > 0;
    }
    
    @Override
    @Transactional
    public int deleteOldNotifications(LocalDateTime before) {
        return jpaNotificationRepository.deleteByCreatedAtBefore(before);
    }
}
