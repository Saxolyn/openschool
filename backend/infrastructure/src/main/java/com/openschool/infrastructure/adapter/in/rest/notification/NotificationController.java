package com.openschool.infrastructure.adapter.in.rest.notification;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.notification.Notification;
import com.openschool.domain.notification.NotificationStatus;
import com.openschool.domain.notification.NotificationType;
import com.openschool.infrastructure.adapter.in.rest.notification.dto.MarkAsReadRequest;
import com.openschool.infrastructure.adapter.in.rest.notification.dto.SendInternalMessageRequest;
import com.openschool.infrastructure.adapter.in.rest.notification.mapper.NotificationDtoMapper;
import com.openschool.notification.port.in.*;
import com.openschool.notification.port.in.command.MarkAsReadCommand;
import com.openschool.notification.port.in.command.SendInternalMessageCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * REST controller for notification operations
 */
@Slf4j
@RestController
@RequestMapping("/api/notifications")
@AllArgsConstructor
@Tag(name = "Notifications", description = "Notification management APIs")
public class NotificationController {
    
    private final GetNotificationsUseCase getNotificationsUseCase;
    private final SendInternalMessageUseCase sendInternalMessageUseCase;
    private final MarkAsReadUseCase markAsReadUseCase;
    private final GetUnreadCountUseCase getUnreadCountUseCase;
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get notifications for a user")
    public ResponseEntity<PageResult<Notification>> getNotificationsForUser(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        PageInfo pageInfo = new PageInfo(page, size);
        PageResult<Notification> notifications = getNotificationsUseCase.getNotificationsForUser(userId, pageInfo);
        
        return ResponseEntity.ok(notifications);
    }
    
    @GetMapping("/user/{userId}/type/{type}")
    @Operation(summary = "Get notifications by type for a user")
    public ResponseEntity<PageResult<Notification>> getNotificationsByType(
            @PathVariable UUID userId,
            @PathVariable NotificationType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        PageInfo pageInfo = new PageInfo(page, size);
        PageResult<Notification> notifications = getNotificationsUseCase.getNotificationsByType(userId, type, pageInfo);
        
        return ResponseEntity.ok(notifications);
    }
    
    @GetMapping("/user/{userId}/status/{status}")
    @Operation(summary = "Get notifications by status for a user")
    public ResponseEntity<PageResult<Notification>> getNotificationsByStatus(
            @PathVariable UUID userId,
            @PathVariable NotificationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        PageInfo pageInfo = new PageInfo(page, size);
        PageResult<Notification> notifications = getNotificationsUseCase.getNotificationsByStatus(userId, status, pageInfo);
        
        return ResponseEntity.ok(notifications);
    }
    
    @GetMapping("/user/{userId}/unread")
    @Operation(summary = "Get unread notifications for a user")
    public ResponseEntity<PageResult<Notification>> getUnreadNotifications(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        PageInfo pageInfo = new PageInfo(page, size);
        PageResult<Notification> notifications = getNotificationsUseCase.getUnreadNotifications(userId, pageInfo);
        
        return ResponseEntity.ok(notifications);
    }
    
    @GetMapping("/user/{userId}/recent")
    @Operation(summary = "Get recent notifications for a user")
    public ResponseEntity<List<Notification>> getRecentNotifications(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "10") int limit) {
        
        List<Notification> notifications = getNotificationsUseCase.getRecentNotifications(userId, limit);
        
        return ResponseEntity.ok(notifications);
    }
    
    @GetMapping("/user/{userId}/category/{category}")
    @Operation(summary = "Get notifications by category for a user")
    public ResponseEntity<PageResult<Notification>> getNotificationsByCategory(
            @PathVariable UUID userId,
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        PageInfo pageInfo = new PageInfo(page, size);
        PageResult<Notification> notifications = getNotificationsUseCase.getNotificationsByCategory(userId, category, pageInfo);
        
        return ResponseEntity.ok(notifications);
    }
    
    @GetMapping("/user/{userId}/unread-count")
    @Operation(summary = "Get unread notification count for a user")
    public ResponseEntity<Long> getUnreadCount(@PathVariable UUID userId) {
        long count = getUnreadCountUseCase.getUnreadCount(userId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/user/{userId}/unread-count/by-type")
    @Operation(summary = "Get unread notification count by type for a user")
    public ResponseEntity<Map<NotificationType, Long>> getUnreadCountByType(@PathVariable UUID userId) {
        Map<NotificationType, Long> counts = getUnreadCountUseCase.getUnreadCountByType(userId);
        return ResponseEntity.ok(counts);
    }
    
    @GetMapping("/user/{userId}/unread-count/by-category")
    @Operation(summary = "Get unread notification count by category for a user")
    public ResponseEntity<Map<String, Long>> getUnreadCountByCategory(@PathVariable UUID userId) {
        Map<String, Long> counts = getUnreadCountUseCase.getUnreadCountByCategory(userId);
        return ResponseEntity.ok(counts);
    }
    
    @PostMapping("/internal-message")
    @Operation(summary = "Send internal message notification")
    public ResponseEntity<Notification> sendInternalMessage(@RequestBody SendInternalMessageRequest request) {
        SendInternalMessageCommand command = NotificationDtoMapper.toCommand(request);
        Notification notification = sendInternalMessageUseCase.sendInternalMessage(command);
        
        return ResponseEntity.ok(notification);
    }
    
    @PostMapping("/internal-message/bulk")
    @Operation(summary = "Send bulk internal message notifications")
    public ResponseEntity<List<Notification>> sendBulkInternalMessage(@RequestBody SendInternalMessageRequest request) {
        SendInternalMessageCommand command = NotificationDtoMapper.toCommand(request);
        List<Notification> notifications = sendInternalMessageUseCase.sendBulkInternalMessage(command);
        
        return ResponseEntity.ok(notifications);
    }
    
    @PutMapping("/{notificationId}/mark-as-read")
    @Operation(summary = "Mark a notification as read")
    public ResponseEntity<Boolean> markAsRead(
            @PathVariable UUID notificationId,
            @RequestBody MarkAsReadRequest request) {
        
        MarkAsReadCommand command = MarkAsReadCommand.builder()
            .notificationId(notificationId)
            .userId(request.getUserId())
            .build();
        
        boolean result = markAsReadUseCase.markAsRead(command);
        return ResponseEntity.ok(result);
    }
    
    @PutMapping("/mark-as-read/multiple")
    @Operation(summary = "Mark multiple notifications as read")
    public ResponseEntity<Integer> markMultipleAsRead(@RequestBody MarkAsReadRequest request) {
        MarkAsReadCommand command = NotificationDtoMapper.toMarkAsReadCommand(request);
        int count = markAsReadUseCase.markMultipleAsRead(command);
        
        return ResponseEntity.ok(count);
    }
    
    @PutMapping("/user/{userId}/mark-all-as-read")
    @Operation(summary = "Mark all notifications as read for a user")
    public ResponseEntity<Integer> markAllAsRead(
            @PathVariable UUID userId,
            @RequestParam(required = false) String category) {
        
        MarkAsReadCommand command = MarkAsReadCommand.builder()
            .userId(userId)
            .markAllAsRead(true)
            .category(category)
            .build();
        
        int count = markAsReadUseCase.markAllAsRead(command);
        return ResponseEntity.ok(count);
    }
}
