package com.openschool.notification.port.in.command;

import com.openschool.domain.notification.NotificationPriority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Command for sending internal message notifications
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class SendInternalMessageCommand {
    
    /**
     * ID of the recipient user
     */
    private UUID recipientId;
    
    /**
     * List of recipient user IDs for bulk messages
     */
    private List<UUID> recipientIds;
    
    /**
     * Title of the message
     */
    private String title;
    
    /**
     * Content of the message
     */
    private String content;
    
    /**
     * Priority of the message
     */
    private NotificationPriority priority;
    
    /**
     * ID of the user sending the message
     */
    private UUID senderId;
    
    /**
     * Category for the notification (e.g., "ACADEMIC", "ADMINISTRATIVE")
     */
    private String category;
    
    /**
     * Additional metadata
     */
    private Map<String, Object> metadata;
    
    /**
     * Whether this is a system message (no sender)
     */
    private boolean isSystemMessage;
    
    /**
     * Whether to send push notification as well
     */
    private boolean sendPushNotification;
    
    /**
     * Action URL for the notification (optional)
     */
    private String actionUrl;
    
    /**
     * Action button text (optional)
     */
    private String actionText;
}
