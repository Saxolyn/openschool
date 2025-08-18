package com.openschool.infrastructure.adapter.in.rest.notification.mapper;

import com.openschool.infrastructure.adapter.in.rest.notification.dto.MarkAsReadRequest;
import com.openschool.infrastructure.adapter.in.rest.notification.dto.SendInternalMessageRequest;
import com.openschool.system.notification.port.in.command.MarkAsReadCommand;
import com.openschool.system.notification.port.in.command.SendInternalMessageCommand;

/**
 * Mapper for notification DTOs
 */
public class NotificationDtoMapper {
    
    /**
     * Convert SendInternalMessageRequest to SendInternalMessageCommand
     */
    public static SendInternalMessageCommand toCommand(SendInternalMessageRequest request) {
        return SendInternalMessageCommand.builder()
            .recipientId(request.getRecipientId())
            .recipientIds(request.getRecipientIds())
            .title(request.getTitle())
            .content(request.getContent())
            .priority(request.getPriority())
            .senderId(request.getSenderId())
            .category(request.getCategory())
            .metadata(request.getMetadata())
            .isSystemMessage(request.isSystemMessage())
            .sendPushNotification(request.isSendPushNotification())
            .actionUrl(request.getActionUrl())
            .actionText(request.getActionText())
            .build();
    }
    
    /**
     * Convert MarkAsReadRequest to MarkAsReadCommand
     */
    public static MarkAsReadCommand toMarkAsReadCommand(MarkAsReadRequest request) {
        return MarkAsReadCommand.builder()
            .notificationId(request.getNotificationId())
            .notificationIds(request.getNotificationIds())
            .userId(request.getUserId())
            .markAllAsRead(request.isMarkAllAsRead())
            .category(request.getCategory())
            .build();
    }
}
