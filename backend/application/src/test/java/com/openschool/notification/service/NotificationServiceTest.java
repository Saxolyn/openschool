package com.openschool.notification.service;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.notification.*;
import com.openschool.notification.exception.EmailSendingException;
import com.openschool.notification.exception.InvalidRecipientException;
import com.openschool.notification.port.in.command.SendEmailCommand;
import com.openschool.notification.port.in.command.SendInternalMessageCommand;
import com.openschool.notification.port.out.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for NotificationService
 */
@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {
    
    @Mock
    private NotificationRepositoryPort notificationRepository;
    
    @Mock
    private EmailTemplateRepositoryPort emailTemplateRepository;
    
    @Mock
    private NotificationPreferenceRepositoryPort preferenceRepository;
    
    @Mock
    private EmailServicePort emailService;
    
    @Mock
    private PushNotificationServicePort pushNotificationService;
    
    private NotificationService notificationService;
    
    @BeforeEach
    void setUp() {
        notificationService = new NotificationService(
            notificationRepository,
            emailTemplateRepository,
            preferenceRepository,
            emailService,
            pushNotificationService
        );
    }
    
    @Test
    void sendEmail_WithValidCommand_ShouldSendSuccessfully() {
        // Given
        UUID recipientId = UUID.randomUUID();
        SendEmailCommand command = SendEmailCommand.builder()
            .to("test@example.com")
            .subject("Test Subject")
            .content("Test Content")
            .isHtml(false)
            .recipientId(recipientId)
            .priority(NotificationPriority.NORMAL)
            .build();
        
        NotificationPreference preference = NotificationPreference.builder()
            .userId(recipientId)
            .emailEnabled(true)
            .build();
        
        Notification savedNotification = Notification.builder()
            .id(UUID.randomUUID())
            .recipientId(recipientId)
            .title("Test Subject")
            .content("Test Content")
            .type(NotificationType.EMAIL)
            .status(NotificationStatus.SENT)
            .build();
        
        when(emailService.isValidEmail("test@example.com")).thenReturn(true);
        when(emailService.isServiceAvailable()).thenReturn(true);
        when(preferenceRepository.findByUserId(recipientId)).thenReturn(Optional.of(preference));
        when(notificationRepository.save(any(Notification.class))).thenReturn(savedNotification);
        when(emailService.sendEmail(eq("test@example.com"), eq("Test Subject"), eq("Test Content"), eq(false)))
            .thenReturn(true);
        
        // When
        Notification result = notificationService.sendEmail(command);
        
        // Then
        assertNotNull(result);
        assertEquals(NotificationStatus.SENT, result.getStatus());
        verify(emailService).sendEmail("test@example.com", "Test Subject", "Test Content", false);
        verify(notificationRepository, times(2)).save(any(Notification.class));
    }
    
    @Test
    void sendEmail_WithInvalidEmail_ShouldThrowException() {
        // Given
        SendEmailCommand command = SendEmailCommand.builder()
            .to("invalid-email")
            .subject("Test Subject")
            .content("Test Content")
            .build();
        
        when(emailService.isValidEmail("invalid-email")).thenReturn(false);
        
        // When & Then
        assertThrows(InvalidRecipientException.class, () -> notificationService.sendEmail(command));
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString(), anyBoolean());
    }
    
    @Test
    void sendEmail_WithEmailServiceUnavailable_ShouldThrowException() {
        // Given
        SendEmailCommand command = SendEmailCommand.builder()
            .to("test@example.com")
            .subject("Test Subject")
            .content("Test Content")
            .build();
        
        when(emailService.isValidEmail("test@example.com")).thenReturn(true);
        when(emailService.isServiceAvailable()).thenReturn(false);
        
        // When & Then
        assertThrows(EmailSendingException.class, () -> notificationService.sendEmail(command));
    }
    
    @Test
    void sendEmail_WithEmailDisabledInPreferences_ShouldCancelNotification() {
        // Given
        UUID recipientId = UUID.randomUUID();
        SendEmailCommand command = SendEmailCommand.builder()
            .to("test@example.com")
            .subject("Test Subject")
            .content("Test Content")
            .recipientId(recipientId)
            .build();
        
        NotificationPreference preference = NotificationPreference.builder()
            .userId(recipientId)
            .emailEnabled(false)
            .build();
        
        Notification cancelledNotification = Notification.builder()
            .id(UUID.randomUUID())
            .status(NotificationStatus.CANCELLED)
            .build();
        
        when(emailService.isValidEmail("test@example.com")).thenReturn(true);
        when(emailService.isServiceAvailable()).thenReturn(true);
        when(preferenceRepository.findByUserId(recipientId)).thenReturn(Optional.of(preference));
        when(notificationRepository.save(any(Notification.class))).thenReturn(cancelledNotification);
        
        // When
        Notification result = notificationService.sendEmail(command);
        
        // Then
        assertEquals(NotificationStatus.CANCELLED, result.getStatus());
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString(), anyBoolean());
    }
    
    @Test
    void sendInternalMessage_WithValidCommand_ShouldSendSuccessfully() {
        // Given
        UUID recipientId = UUID.randomUUID();
        UUID senderId = UUID.randomUUID();
        
        SendInternalMessageCommand command = SendInternalMessageCommand.builder()
            .recipientId(recipientId)
            .senderId(senderId)
            .title("Test Message")
            .content("Test Content")
            .priority(NotificationPriority.NORMAL)
            .sendPushNotification(true)
            .build();
        
        NotificationPreference preference = NotificationPreference.builder()
            .userId(recipientId)
            .internalMessageEnabled(true)
            .build();
        
        Notification savedNotification = Notification.builder()
            .id(UUID.randomUUID())
            .recipientId(recipientId)
            .senderId(senderId)
            .title("Test Message")
            .content("Test Content")
            .type(NotificationType.INTERNAL_MESSAGE)
            .status(NotificationStatus.SENT)
            .build();
        
        when(preferenceRepository.findByUserId(recipientId)).thenReturn(Optional.of(preference));
        when(notificationRepository.save(any(Notification.class))).thenReturn(savedNotification);
        when(pushNotificationService.isServiceAvailable()).thenReturn(true);
        when(pushNotificationService.sendPushNotification(eq(recipientId), eq("Test Message"), 
            eq("Test Content"), any(Map.class))).thenReturn(true);
        
        // When
        Notification result = notificationService.sendInternalMessage(command);
        
        // Then
        assertNotNull(result);
        assertEquals(NotificationStatus.SENT, result.getStatus());
        verify(pushNotificationService).sendPushNotification(eq(recipientId), eq("Test Message"), 
            eq("Test Content"), any(Map.class));
        verify(notificationRepository, times(2)).save(any(Notification.class));
    }
    
    @Test
    void sendInternalMessage_WithNullRecipient_ShouldThrowException() {
        // Given
        SendInternalMessageCommand command = SendInternalMessageCommand.builder()
            .recipientId(null)
            .title("Test Message")
            .content("Test Content")
            .build();
        
        // When & Then
        assertThrows(InvalidRecipientException.class, 
            () -> notificationService.sendInternalMessage(command));
    }
    
    @Test
    void getNotificationsForUser_ShouldReturnPagedResults() {
        // Given
        UUID userId = UUID.randomUUID();
        PageInfo pageInfo = new PageInfo(0, 10);
        
        List<Notification> notifications = Arrays.asList(
            Notification.builder().id(UUID.randomUUID()).recipientId(userId).build(),
            Notification.builder().id(UUID.randomUUID()).recipientId(userId).build()
        );
        
        PageResult<Notification> expectedResult = new PageResult<>(notifications, 2L, 1, 0);
        
        when(notificationRepository.findByRecipientId(userId, pageInfo)).thenReturn(expectedResult);
        
        // When
        PageResult<Notification> result = notificationService.getNotificationsForUser(userId, pageInfo);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(2L, result.getTotalElements());
        verify(notificationRepository).findByRecipientId(userId, pageInfo);
    }
    
    @Test
    void getUnreadCount_ShouldReturnCorrectCount() {
        // Given
        UUID userId = UUID.randomUUID();
        long expectedCount = 5L;
        
        when(notificationRepository.countUnreadByRecipientId(userId)).thenReturn(expectedCount);
        
        // When
        long result = notificationService.getUnreadCount(userId);
        
        // Then
        assertEquals(expectedCount, result);
        verify(notificationRepository).countUnreadByRecipientId(userId);
    }
    
    @Test
    void markAsRead_WithValidNotification_ShouldReturnTrue() {
        // Given
        UUID notificationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        
        when(notificationRepository.markAsRead(notificationId, userId)).thenReturn(true);
        
        // When
        boolean result = notificationService.markAsRead(
            com.openschool.notification.port.in.command.MarkAsReadCommand.builder()
                .notificationId(notificationId)
                .userId(userId)
                .build()
        );
        
        // Then
        assertTrue(result);
        verify(notificationRepository).markAsRead(notificationId, userId);
    }
    
    @Test
    void sendBulkInternalMessage_ShouldSendToAllRecipients() {
        // Given
        UUID senderId = UUID.randomUUID();
        List<UUID> recipientIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        
        SendInternalMessageCommand command = SendInternalMessageCommand.builder()
            .recipientIds(recipientIds)
            .senderId(senderId)
            .title("Bulk Message")
            .content("Bulk Content")
            .build();
        
        // Mock preferences for all recipients
        NotificationPreference preference = NotificationPreference.builder()
            .internalMessageEnabled(true)
            .build();
        
        for (UUID recipientId : recipientIds) {
            when(preferenceRepository.findByUserId(recipientId)).thenReturn(Optional.of(preference));
            when(notificationRepository.save(any(Notification.class)))
                .thenReturn(Notification.builder()
                    .id(UUID.randomUUID())
                    .recipientId(recipientId)
                    .status(NotificationStatus.SENT)
                    .build());
        }
        
        // When
        List<Notification> results = notificationService.sendBulkInternalMessage(command);
        
        // Then
        assertEquals(3, results.size());
        verify(notificationRepository, times(6)).save(any(Notification.class)); // 2 saves per recipient
    }
}
