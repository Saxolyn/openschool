package com.openschool.notification.port.in.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

/**
 * Command for marking notifications as read
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class MarkAsReadCommand {
    
    /**
     * ID of the notification to mark as read
     */
    private UUID notificationId;
    
    /**
     * List of notification IDs to mark as read (for bulk operations)
     */
    private List<UUID> notificationIds;
    
    /**
     * ID of the user marking the notification as read
     */
    private UUID userId;
    
    /**
     * Whether to mark all notifications for the user as read
     */
    private boolean markAllAsRead;
    
    /**
     * Category to mark as read (if markAllAsRead is true)
     */
    private String category;
}
