package com.openschool.infrastructure.adapter.out.persistence.notification.entity;

import com.openschool.domain.notification.Notification;
import com.openschool.domain.notification.NotificationPriority;
import com.openschool.domain.notification.NotificationStatus;
import com.openschool.domain.notification.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * JPA entity for notifications
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications", indexes = {
    @Index(name = "idx_notification_recipient", columnList = "recipient_id"),
    @Index(name = "idx_notification_status", columnList = "status"),
    @Index(name = "idx_notification_type", columnList = "type"),
    @Index(name = "idx_notification_created", columnList = "created_at"),
    @Index(name = "idx_notification_recipient_status", columnList = "recipient_id, status"),
    @Index(name = "idx_notification_recipient_type", columnList = "recipient_id, type")
})
public class NotificationEntity {
    
    @Id
    private UUID id;
    
    @Column(name = "recipient_id", nullable = false)
    private UUID recipientId;
    
    @Column(name = "sender_id")
    private UUID senderId;
    
    @Column(name = "title", nullable = false, length = 500)
    private String title;
    
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType type;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NotificationStatus status;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private NotificationPriority priority;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "template_id")
    private UUID templateId;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "template_variables", columnDefinition = "jsonb")
    private Map<String, Object> templateVariables;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private Map<String, Object> metadata;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "sent_at")
    private LocalDateTime sentAt;
    
    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;
    
    @Column(name = "read_at")
    private LocalDateTime readAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    @Column(name = "retry_count")
    private Integer retryCount;
    
    @Column(name = "max_retries")
    private Integer maxRetries;
    
    @Column(name = "retry_at")
    private LocalDateTime retryAt;
    
    /**
     * Convert entity to domain object
     */
    public Notification toDomain() {
        return Notification.builder()
            .id(this.id)
            .recipientId(this.recipientId)
            .senderId(this.senderId)
            .title(this.title)
            .content(this.content)
            .type(this.type)
            .status(this.status)
            .priority(this.priority)
            .email(this.email)
            .templateId(this.templateId)
            .templateVariables(this.templateVariables)
            .metadata(this.metadata)
            .createdAt(this.createdAt)
            .sentAt(this.sentAt)
            .deliveredAt(this.deliveredAt)
            .readAt(this.readAt)
            .updatedAt(this.updatedAt)
            .errorMessage(this.errorMessage)
            .retryCount(this.retryCount)
            .maxRetries(this.maxRetries)
            .retryAt(this.retryAt)
            .build();
    }
    
    /**
     * Create entity from domain object
     */
    public static NotificationEntity fromDomain(Notification notification) {
        return NotificationEntity.builder()
            .id(notification.getId())
            .recipientId(notification.getRecipientId())
            .senderId(notification.getSenderId())
            .title(notification.getTitle())
            .content(notification.getContent())
            .type(notification.getType())
            .status(notification.getStatus())
            .priority(notification.getPriority())
            .email(notification.getEmail())
            .templateId(notification.getTemplateId())
            .templateVariables(notification.getTemplateVariables())
            .metadata(notification.getMetadata())
            .createdAt(notification.getCreatedAt())
            .sentAt(notification.getSentAt())
            .deliveredAt(notification.getDeliveredAt())
            .readAt(notification.getReadAt())
            .updatedAt(notification.getUpdatedAt())
            .errorMessage(notification.getErrorMessage())
            .retryCount(notification.getRetryCount())
            .maxRetries(notification.getMaxRetries())
            .retryAt(notification.getRetryAt())
            .build();
    }
    
    /**
     * Create reference-only entity
     */
    public static NotificationEntity referenceOnly(UUID id) {
        NotificationEntity entity = new NotificationEntity();
        entity.setId(id);
        return entity;
    }
}
