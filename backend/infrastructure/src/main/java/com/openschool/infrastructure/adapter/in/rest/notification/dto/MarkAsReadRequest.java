package com.openschool.infrastructure.adapter.in.rest.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * Request DTO for marking notifications as read
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarkAsReadRequest {
    
    private UUID notificationId;
    private List<UUID> notificationIds;
    private UUID userId;
    private boolean markAllAsRead;
    private String category;
}
