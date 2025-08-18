package com.openschool.infrastructure.adapter.out.persistence.notification.repository;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.notification.Notification;
import com.openschool.domain.notification.NotificationStatus;
import com.openschool.domain.notification.NotificationType;
import com.openschool.infrastructure.adapter.out.persistence.notification.entity.NotificationEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for NotificationRepositoryAdapter
 */
@ExtendWith(MockitoExtension.class)
class NotificationRepositoryAdapterTest {
    
    @Mock
    private JpaNotificationRepository jpaNotificationRepository;
    
    private NotificationRepositoryAdapter notificationRepositoryAdapter;
    
    @BeforeEach
    void setUp() {
        notificationRepositoryAdapter = new NotificationRepositoryAdapter(jpaNotificationRepository);
    }
    
    @Test
    void save_ShouldSaveAndReturnNotification() {
        // Given
        UUID notificationId = UUID.randomUUID();
        UUID recipientId = UUID.randomUUID();
        
        Notification notification = Notification.builder()
            .id(notificationId)
            .recipientId(recipientId)
            .title("Test Notification")
            .content("Test Content")
            .type(NotificationType.INTERNAL_MESSAGE)
            .status(NotificationStatus.PENDING)
            .createdAt(LocalDateTime.now())
            .build();
        
        NotificationEntity entity = NotificationEntity.fromDomain(notification);
        NotificationEntity savedEntity = NotificationEntity.fromDomain(notification);
        savedEntity.setId(notificationId);
        
        when(jpaNotificationRepository.save(any(NotificationEntity.class))).thenReturn(savedEntity);
        
        // When
        Notification result = notificationRepositoryAdapter.save(notification);
        
        // Then
        assertNotNull(result);
        assertEquals(notificationId, result.getId());
        assertEquals(recipientId, result.getRecipientId());
        assertEquals("Test Notification", result.getTitle());
        verify(jpaNotificationRepository).save(any(NotificationEntity.class));
    }
    
    @Test
    void findById_WithExistingId_ShouldReturnNotification() {
        // Given
        UUID notificationId = UUID.randomUUID();
        NotificationEntity entity = NotificationEntity.builder()
            .id(notificationId)
            .recipientId(UUID.randomUUID())
            .title("Test Notification")
            .type(NotificationType.EMAIL)
            .status(NotificationStatus.SENT)
            .build();
        
        when(jpaNotificationRepository.findById(notificationId)).thenReturn(Optional.of(entity));
        
        // When
        Optional<Notification> result = notificationRepositoryAdapter.findById(notificationId);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals(notificationId, result.get().getId());
        assertEquals("Test Notification", result.get().getTitle());
        verify(jpaNotificationRepository).findById(notificationId);
    }
    
    @Test
    void findById_WithNonExistentId_ShouldReturnEmpty() {
        // Given
        UUID notificationId = UUID.randomUUID();
        when(jpaNotificationRepository.findById(notificationId)).thenReturn(Optional.empty());
        
        // When
        Optional<Notification> result = notificationRepositoryAdapter.findById(notificationId);
        
        // Then
        assertFalse(result.isPresent());
        verify(jpaNotificationRepository).findById(notificationId);
    }
    
    @Test
    void findByRecipientId_ShouldReturnPagedResults() {
        // Given
        UUID recipientId = UUID.randomUUID();
        PageInfo pageInfo = new PageInfo(0, 10);
        Pageable pageable = PageRequest.of(0, 10);
        
        List<NotificationEntity> entities = Arrays.asList(
            NotificationEntity.builder()
                .id(UUID.randomUUID())
                .recipientId(recipientId)
                .title("Notification 1")
                .type(NotificationType.EMAIL)
                .status(NotificationStatus.SENT)
                .build(),
            NotificationEntity.builder()
                .id(UUID.randomUUID())
                .recipientId(recipientId)
                .title("Notification 2")
                .type(NotificationType.INTERNAL_MESSAGE)
                .status(NotificationStatus.READ)
                .build()
        );
        
        Page<NotificationEntity> page = new PageImpl<>(entities, pageable, 2);
        when(jpaNotificationRepository.findByRecipientIdOrderByCreatedAtDesc(recipientId, pageable))
            .thenReturn(page);
        
        // When
        PageResult<Notification> result = notificationRepositoryAdapter.findByRecipientId(recipientId, pageInfo);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(2L, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(0, result.getCurrentPage());
        verify(jpaNotificationRepository).findByRecipientIdOrderByCreatedAtDesc(recipientId, pageable);
    }
    
    @Test
    void countUnreadByRecipientId_ShouldReturnCorrectCount() {
        // Given
        UUID recipientId = UUID.randomUUID();
        long expectedCount = 5L;
        
        when(jpaNotificationRepository.countUnreadByRecipientId(recipientId)).thenReturn(expectedCount);
        
        // When
        long result = notificationRepositoryAdapter.countUnreadByRecipientId(recipientId);
        
        // Then
        assertEquals(expectedCount, result);
        verify(jpaNotificationRepository).countUnreadByRecipientId(recipientId);
    }
    
    @Test
    void markAsRead_WithValidData_ShouldReturnTrue() {
        // Given
        UUID notificationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        
        when(jpaNotificationRepository.markAsRead(eq(notificationId), eq(userId), 
            any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(1);
        
        // When
        boolean result = notificationRepositoryAdapter.markAsRead(notificationId, userId);
        
        // Then
        assertTrue(result);
        verify(jpaNotificationRepository).markAsRead(eq(notificationId), eq(userId), 
            any(LocalDateTime.class), any(LocalDateTime.class));
    }
    
    @Test
    void markAsRead_WithNoRowsAffected_ShouldReturnFalse() {
        // Given
        UUID notificationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        
        when(jpaNotificationRepository.markAsRead(eq(notificationId), eq(userId), 
            any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(0);
        
        // When
        boolean result = notificationRepositoryAdapter.markAsRead(notificationId, userId);
        
        // Then
        assertFalse(result);
        verify(jpaNotificationRepository).markAsRead(eq(notificationId), eq(userId), 
            any(LocalDateTime.class), any(LocalDateTime.class));
    }
    
    @Test
    void markMultipleAsRead_ShouldReturnCorrectCount() {
        // Given
        List<UUID> notificationIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        UUID userId = UUID.randomUUID();
        int expectedCount = 3;
        
        when(jpaNotificationRepository.markMultipleAsRead(eq(notificationIds), eq(userId), 
            any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(expectedCount);
        
        // When
        int result = notificationRepositoryAdapter.markMultipleAsRead(notificationIds, userId);
        
        // Then
        assertEquals(expectedCount, result);
        verify(jpaNotificationRepository).markMultipleAsRead(eq(notificationIds), eq(userId), 
            any(LocalDateTime.class), any(LocalDateTime.class));
    }
    
    @Test
    void countUnreadByRecipientIdGroupByType_ShouldReturnCorrectMap() {
        // Given
        UUID recipientId = UUID.randomUUID();
        List<Object[]> queryResults = Arrays.asList(
            new Object[]{"EMAIL", 3L},
            new Object[]{"INTERNAL_MESSAGE", 2L}
        );
        
        when(jpaNotificationRepository.countUnreadByRecipientIdGroupByType(recipientId))
            .thenReturn(queryResults);
        
        // When
        Map<NotificationType, Long> result = notificationRepositoryAdapter
            .countUnreadByRecipientIdGroupByType(recipientId);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(3L, result.get(NotificationType.EMAIL));
        assertEquals(2L, result.get(NotificationType.INTERNAL_MESSAGE));
        verify(jpaNotificationRepository).countUnreadByRecipientIdGroupByType(recipientId);
    }
    
    @Test
    void countUnreadByRecipientIdGroupByCategory_ShouldReturnCorrectMap() {
        // Given
        UUID recipientId = UUID.randomUUID();
        List<Object[]> queryResults = Arrays.asList(
            new Object[]{"ACADEMIC", 4L},
            new Object[]{"ADMINISTRATIVE", 1L}
        );
        
        when(jpaNotificationRepository.countUnreadByRecipientIdGroupByCategory(recipientId))
            .thenReturn(queryResults);
        
        // When
        Map<String, Long> result = notificationRepositoryAdapter
            .countUnreadByRecipientIdGroupByCategory(recipientId);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(4L, result.get("ACADEMIC"));
        assertEquals(1L, result.get("ADMINISTRATIVE"));
        verify(jpaNotificationRepository).countUnreadByRecipientIdGroupByCategory(recipientId);
    }
    
    @Test
    void findRecentByRecipientId_WithLimitLessThan10_ShouldReturnCorrectList() {
        // Given
        UUID recipientId = UUID.randomUUID();
        int limit = 5;
        
        List<NotificationEntity> entities = Arrays.asList(
            NotificationEntity.builder().id(UUID.randomUUID()).recipientId(recipientId).build(),
            NotificationEntity.builder().id(UUID.randomUUID()).recipientId(recipientId).build(),
            NotificationEntity.builder().id(UUID.randomUUID()).recipientId(recipientId).build(),
            NotificationEntity.builder().id(UUID.randomUUID()).recipientId(recipientId).build(),
            NotificationEntity.builder().id(UUID.randomUUID()).recipientId(recipientId).build(),
            NotificationEntity.builder().id(UUID.randomUUID()).recipientId(recipientId).build(),
            NotificationEntity.builder().id(UUID.randomUUID()).recipientId(recipientId).build()
        );
        
        when(jpaNotificationRepository.findTop10ByRecipientIdOrderByCreatedAtDesc(recipientId))
            .thenReturn(entities);
        
        // When
        List<Notification> result = notificationRepositoryAdapter.findRecentByRecipientId(recipientId, limit);
        
        // Then
        assertNotNull(result);
        assertEquals(limit, result.size());
        verify(jpaNotificationRepository).findTop10ByRecipientIdOrderByCreatedAtDesc(recipientId);
    }
    
    @Test
    void updateStatus_ShouldReturnTrue() {
        // Given
        UUID notificationId = UUID.randomUUID();
        NotificationStatus status = NotificationStatus.SENT;
        
        when(jpaNotificationRepository.updateStatus(eq(notificationId), eq(status), 
            any(LocalDateTime.class))).thenReturn(1);
        
        // When
        boolean result = notificationRepositoryAdapter.updateStatus(notificationId, status);
        
        // Then
        assertTrue(result);
        verify(jpaNotificationRepository).updateStatus(eq(notificationId), eq(status), 
            any(LocalDateTime.class));
    }
}
