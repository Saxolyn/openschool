package com.openschool.infrastructure.adapter.out.persistence.notification.repository;

import com.openschool.infrastructure.adapter.out.persistence.notification.entity.NotificationPreferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA repository for notification preferences
 */
@Repository
public interface JpaNotificationPreferenceRepository extends JpaRepository<NotificationPreferenceEntity, UUID> {
    
    /**
     * Find preferences by user ID
     */
    Optional<NotificationPreferenceEntity> findByUserId(UUID userId);
    
    /**
     * Check if preferences exist for a user
     */
    boolean existsByUserId(UUID userId);
    
    /**
     * Delete preferences by user ID
     */
    void deleteByUserId(UUID userId);
}
