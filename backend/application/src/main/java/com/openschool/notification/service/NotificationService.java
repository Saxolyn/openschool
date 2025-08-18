package com.openschool.notification.service;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.notification.*;
import com.openschool.notification.exception.ExceptionMessage;
import com.openschool.notification.exception.NotificationException;
import com.openschool.notification.exception.EmailSendingException;
import com.openschool.notification.exception.InvalidRecipientException;
import com.openschool.notification.port.in.*;
import com.openschool.notification.port.in.command.*;
import com.openschool.notification.port.out.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service implementing notification use cases
 */
@Slf4j
@AllArgsConstructor
public class NotificationService implements 
        SendEmailUseCase,
        SendInternalMessageUseCase,
        GetNotificationsUseCase,
        MarkAsReadUseCase,
        GetUnreadCountUseCase {
    
    private final NotificationRepositoryPort notificationRepository;
    private final EmailTemplateRepositoryPort emailTemplateRepository;
    private final NotificationPreferenceRepositoryPort preferenceRepository;
    private final EmailServicePort emailService;
    private final PushNotificationServicePort pushNotificationService;
    
    @Override
    public Notification sendEmail(SendEmailCommand command) {
        log.info("Sending email to: {}", command.getTo());
        
        // Validate email address
        if (!emailService.isValidEmail(command.getTo())) {
            throw new InvalidRecipientException(ExceptionMessage.EMAIL_INVALID_ADDRESS.getMessage());
        }
        
        // Check if email service is available
        if (!emailService.isServiceAvailable()) {
            throw new EmailSendingException(ExceptionMessage.EMAIL_SERVICE_UNAVAILABLE.getMessage());
        }
        
        // Check user preferences if recipient ID is provided
        if (command.getRecipientId() != null) {
            Optional<NotificationPreference> preference = preferenceRepository.findByUserId(command.getRecipientId());
            if (preference.isPresent() && !preference.get().isNotificationTypeEnabled(NotificationType.EMAIL)) {
                log.info("Email notifications disabled for user: {}", command.getRecipientId());
                return createNotificationRecord(command, NotificationStatus.CANCELLED, "Email notifications disabled for user");
            }
        }
        
        // Create notification record
        Notification notification = createEmailNotification(command);
        notification = notificationRepository.save(notification);
        
        try {
            // Process template if provided
            String finalSubject = command.getSubject();
            String finalContent = command.getContent();
            
            if (command.getTemplateId() != null) {
                EmailTemplate template = emailTemplateRepository.findById(command.getTemplateId())
                    .orElseThrow(() -> new NotificationException(ExceptionMessage.EMAIL_TEMPLATE_NOT_FOUND.getMessage()));
                
                if (!template.isActive()) {
                    throw new NotificationException(ExceptionMessage.EMAIL_TEMPLATE_INACTIVE.getMessage());
                }
                
                finalSubject = processTemplate(template.getSubject(), command.getTemplateVariables());
                finalContent = processTemplate(template.getContentForType(command.isHtml()), command.getTemplateVariables());
            }
            
            // Send email
            boolean sent;
            if (command.getCc() != null || command.getBcc() != null) {
                sent = emailService.sendEmail(command.getTo(), command.getCc(), command.getBcc(), 
                                            finalSubject, finalContent, command.isHtml());
            } else {
                sent = emailService.sendEmail(command.getTo(), finalSubject, finalContent, command.isHtml());
            }
            
            if (sent) {
                notification.markAsSent();
                log.info("Email sent successfully to: {}", command.getTo());
            } else {
                notification.markAsFailed("Email sending failed");
                log.error("Failed to send email to: {}", command.getTo());
            }
            
        } catch (Exception e) {
            log.error("Error sending email to: {}", command.getTo(), e);
            notification.markAsFailed("Email sending error: " + e.getMessage());
        }
        
        return notificationRepository.save(notification);
    }
    
    @Override
    public Notification sendInternalMessage(SendInternalMessageCommand command) {
        log.info("Sending internal message to user: {}", command.getRecipientId());
        
        if (command.getRecipientId() == null) {
            throw new InvalidRecipientException(ExceptionMessage.RECIPIENT_NOT_FOUND.getMessage());
        }
        
        // Check user preferences
        Optional<NotificationPreference> preference = preferenceRepository.findByUserId(command.getRecipientId());
        if (preference.isPresent() && !preference.get().isNotificationTypeEnabled(NotificationType.INTERNAL_MESSAGE)) {
            log.info("Internal message notifications disabled for user: {}", command.getRecipientId());
            return createNotificationRecord(command, NotificationStatus.CANCELLED, "Internal message notifications disabled for user");
        }
        
        // Create notification
        Notification notification = createInternalMessageNotification(command);
        notification = notificationRepository.save(notification);
        
        try {
            // Mark as sent immediately for internal messages
            notification.markAsSent();
            
            // Send push notification if requested
            if (command.isSendPushNotification() && pushNotificationService.isServiceAvailable()) {
                Map<String, Object> pushData = new HashMap<>();
                pushData.put("notificationId", notification.getId().toString());
                pushData.put("type", "INTERNAL_MESSAGE");
                if (command.getActionUrl() != null) {
                    pushData.put("actionUrl", command.getActionUrl());
                }
                
                pushNotificationService.sendPushNotification(
                    command.getRecipientId(), 
                    command.getTitle(), 
                    command.getContent(), 
                    pushData
                );
            }
            
            log.info("Internal message sent successfully to user: {}", command.getRecipientId());
            
        } catch (Exception e) {
            log.error("Error sending internal message to user: {}", command.getRecipientId(), e);
            notification.markAsFailed("Internal message error: " + e.getMessage());
        }
        
        return notificationRepository.save(notification);
    }
    
    @Override
    public List<Notification> sendBulkInternalMessage(SendInternalMessageCommand command) {
        log.info("Sending bulk internal message to {} users", command.getRecipientIds().size());
        
        if (command.getRecipientIds() == null || command.getRecipientIds().isEmpty()) {
            throw new InvalidRecipientException(ExceptionMessage.RECIPIENT_NOT_FOUND.getMessage());
        }
        
        List<Notification> notifications = new ArrayList<>();
        
        for (UUID recipientId : command.getRecipientIds()) {
            try {
                SendInternalMessageCommand singleCommand = SendInternalMessageCommand.builder()
                    .recipientId(recipientId)
                    .title(command.getTitle())
                    .content(command.getContent())
                    .priority(command.getPriority())
                    .senderId(command.getSenderId())
                    .category(command.getCategory())
                    .metadata(command.getMetadata())
                    .isSystemMessage(command.isSystemMessage())
                    .sendPushNotification(command.isSendPushNotification())
                    .actionUrl(command.getActionUrl())
                    .actionText(command.getActionText())
                    .build();
                
                Notification notification = sendInternalMessage(singleCommand);
                notifications.add(notification);
                
            } catch (Exception e) {
                log.error("Failed to send message to user: {}", recipientId, e);
                // Continue with other recipients
            }
        }
        
        log.info("Bulk internal message completed. Sent to {}/{} users", 
                notifications.size(), command.getRecipientIds().size());
        
        return notifications;
    }
    
    private Notification createEmailNotification(SendEmailCommand command) {
        return Notification.builder()
            .id(UUID.randomUUID())
            .recipientId(command.getRecipientId())
            .senderId(command.getSenderId())
            .title(command.getSubject())
            .content(command.getContent())
            .type(NotificationType.EMAIL)
            .status(NotificationStatus.PENDING)
            .priority(command.getPriority() != null ? command.getPriority() : NotificationPriority.NORMAL)
            .email(command.getTo())
            .templateId(command.getTemplateId())
            .templateVariables(command.getTemplateVariables())
            .metadata(command.getMetadata())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .maxRetries(command.getMaxRetries() != null ? command.getMaxRetries() : 3)
            .retryCount(0)
            .build();
    }
    
    private Notification createInternalMessageNotification(SendInternalMessageCommand command) {
        return Notification.builder()
            .id(UUID.randomUUID())
            .recipientId(command.getRecipientId())
            .senderId(command.getSenderId())
            .title(command.getTitle())
            .content(command.getContent())
            .type(NotificationType.INTERNAL_MESSAGE)
            .status(NotificationStatus.PENDING)
            .priority(command.getPriority() != null ? command.getPriority() : NotificationPriority.NORMAL)
            .metadata(command.getMetadata())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .maxRetries(1) // Internal messages don't need retries
            .retryCount(0)
            .build();
    }
    
    private Notification createNotificationRecord(Object command, NotificationStatus status, String errorMessage) {
        // Helper method to create notification record for cancelled/failed notifications
        if (command instanceof SendEmailCommand) {
            SendEmailCommand emailCmd = (SendEmailCommand) command;
            Notification notification = createEmailNotification(emailCmd);
            notification.setStatus(status);
            notification.setErrorMessage(errorMessage);
            return notification;
        } else if (command instanceof SendInternalMessageCommand) {
            SendInternalMessageCommand msgCmd = (SendInternalMessageCommand) command;
            Notification notification = createInternalMessageNotification(msgCmd);
            notification.setStatus(status);
            notification.setErrorMessage(errorMessage);
            return notification;
        }
        throw new IllegalArgumentException("Unsupported command type");
    }
    
    private String processTemplate(String template, Map<String, Object> variables) {
        if (template == null || variables == null) {
            return template;
        }
        
        String result = template;
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            String value = entry.getValue() != null ? entry.getValue().toString() : "";
            result = result.replace(placeholder, value);
        }
        
        return result;
    }
    
    // Implementation of GetNotificationsUseCase methods
    @Override
    public PageResult<Notification> getNotificationsForUser(UUID userId, PageInfo pageInfo) {
        return notificationRepository.findByRecipientId(userId, pageInfo);
    }
    
    @Override
    public PageResult<Notification> getNotificationsByType(UUID userId, NotificationType type, PageInfo pageInfo) {
        return notificationRepository.findByRecipientIdAndType(userId, type, pageInfo);
    }
    
    @Override
    public PageResult<Notification> getNotificationsByStatus(UUID userId, NotificationStatus status, PageInfo pageInfo) {
        return notificationRepository.findByRecipientIdAndStatus(userId, status, pageInfo);
    }
    
    @Override
    public PageResult<Notification> getUnreadNotifications(UUID userId, PageInfo pageInfo) {
        return notificationRepository.findUnreadByRecipientId(userId, pageInfo);
    }
    
    @Override
    public List<Notification> getRecentNotifications(UUID userId, int limit) {
        return notificationRepository.findRecentByRecipientId(userId, limit);
    }
    
    @Override
    public PageResult<Notification> getNotificationsByCategory(UUID userId, String category, PageInfo pageInfo) {
        return notificationRepository.findByRecipientIdAndCategory(userId, category, pageInfo);
    }
    
    // Implementation of MarkAsReadUseCase methods
    @Override
    public boolean markAsRead(MarkAsReadCommand command) {
        if (command.getNotificationId() == null) {
            throw new NotificationException(ExceptionMessage.INVALID_NOTIFICATION_DATA.getMessage());
        }
        
        return notificationRepository.markAsRead(command.getNotificationId(), command.getUserId());
    }
    
    @Override
    public int markMultipleAsRead(MarkAsReadCommand command) {
        if (command.getNotificationIds() == null || command.getNotificationIds().isEmpty()) {
            throw new NotificationException(ExceptionMessage.INVALID_NOTIFICATION_DATA.getMessage());
        }
        
        return notificationRepository.markMultipleAsRead(command.getNotificationIds(), command.getUserId());
    }
    
    @Override
    public int markAllAsRead(MarkAsReadCommand command) {
        if (command.getCategory() != null) {
            return notificationRepository.markAllAsReadByCategory(command.getUserId(), command.getCategory());
        } else {
            return notificationRepository.markAllAsRead(command.getUserId());
        }
    }
    
    // Implementation of GetUnreadCountUseCase methods
    @Override
    public long getUnreadCount(UUID userId) {
        return notificationRepository.countUnreadByRecipientId(userId);
    }
    
    @Override
    public Map<NotificationType, Long> getUnreadCountByType(UUID userId) {
        return notificationRepository.countUnreadByRecipientIdGroupByType(userId);
    }
    
    @Override
    public Map<String, Long> getUnreadCountByCategory(UUID userId) {
        return notificationRepository.countUnreadByRecipientIdGroupByCategory(userId);
    }
    
    @Override
    public long getUnreadCountForType(UUID userId, NotificationType type) {
        return notificationRepository.countUnreadByRecipientIdAndType(userId, type);
    }
    
    @Override
    public long getUnreadCountForCategory(UUID userId, String category) {
        return notificationRepository.countUnreadByRecipientIdAndCategory(userId, category);
    }
}
