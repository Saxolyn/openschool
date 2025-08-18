package com.openschool.infrastructure.adapter.in.rest.notification.dto;

import com.openschool.domain.notification.NotificationPriority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Request DTO for sending internal message notifications
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendInternalMessageRequest {
    
    private UUID recipientId;
    private List<UUID> recipientIds;
    private String title;
    private String content;
    private NotificationPriority priority;
    private UUID senderId;
    private String category;
    private Map<String, Object> metadata;
    private boolean isSystemMessage;
    private boolean sendPushNotification;
    private String actionUrl;
    private String actionText;
}
