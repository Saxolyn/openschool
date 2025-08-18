package com.openschool.infrastructure.adapter.out.persistence.notification.repository;

import com.openschool.domain.notification.NotificationPreference;
import com.openschool.infrastructure.adapter.out.persistence.notification.entity.NotificationPreferenceEntity;
import com.openschool.notification.port.out.NotificationPreferenceRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA implementation of NotificationPreferenceRepositoryPort
 */
@Repository
@AllArgsConstructor
public class NotificationPreferenceRepositoryAdapter implements NotificationPreferenceRepositoryPort {
    
    private final JpaNotificationPreferenceRepository jpaNotificationPreferenceRepository;
    
    @Override
    public NotificationPreference save(NotificationPreference preference) {
        NotificationPreferenceEntity entity = NotificationPreferenceEntity.fromDomain(preference);
        NotificationPreferenceEntity savedEntity = jpaNotificationPreferenceRepository.save(entity);
        return savedEntity.toDomain();
    }
    
    @Override
    public Optional<NotificationPreference> findByUserId(UUID userId) {
        return jpaNotificationPreferenceRepository.findByUserId(userId)
            .map(NotificationPreferenceEntity::toDomain);
    }
    
    @Override
    public boolean existsByUserId(UUID userId) {
        return jpaNotificationPreferenceRepository.existsByUserId(userId);
    }
    
    @Override
    @Transactional
    public boolean deleteByUserId(UUID userId) {
        if (jpaNotificationPreferenceRepository.existsByUserId(userId)) {
            jpaNotificationPreferenceRepository.deleteByUserId(userId);
            return true;
        }
        return false;
    }
}
