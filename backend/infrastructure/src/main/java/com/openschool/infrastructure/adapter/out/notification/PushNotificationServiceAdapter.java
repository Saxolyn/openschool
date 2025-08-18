package com.openschool.infrastructure.adapter.out.notification;

import com.openschool.notification.port.out.PushNotificationServicePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * WebSocket implementation of PushNotificationServicePort
 * This is a simplified implementation using WebSocket for real-time notifications
 * In production, you might want to integrate with Firebase Cloud Messaging (FCM),
 * Apple Push Notification Service (APNs), or other push notification services
 */
@Slf4j
@Service
public class PushNotificationServiceAdapter implements PushNotificationServicePort {
    
    private final SimpMessagingTemplate messagingTemplate;
    
    // In-memory storage for device tokens (in production, use a database)
    private final Map<UUID, Map<String, String>> userDeviceTokens = new HashMap<>();
    
    public PushNotificationServiceAdapter(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    
    @Override
    public boolean sendPushNotification(UUID userId, String title, String message, Map<String, Object> data) {
        try {
            log.info("Sending push notification to user: {}", userId);
            
            // Create notification payload
            Map<String, Object> notification = new HashMap<>();
            notification.put("title", title);
            notification.put("message", message);
            notification.put("timestamp", System.currentTimeMillis());
            notification.put("data", data);
            
            // Send via WebSocket to specific user
            String destination = "/user/" + userId + "/notifications";
            messagingTemplate.convertAndSend(destination, notification);
            
            log.info("Push notification sent successfully to user: {}", userId);
            return true;
            
        } catch (Exception e) {
            log.error("Failed to send push notification to user: {}", userId, e);
            return false;
        }
    }
    
    @Override
    public int sendBulkPushNotification(List<UUID> userIds, String title, String message, Map<String, Object> data) {
        if (userIds == null || userIds.isEmpty()) {
            return 0;
        }
        
        int successCount = 0;
        
        for (UUID userId : userIds) {
            try {
                if (sendPushNotification(userId, title, message, data)) {
                    successCount++;
                }
            } catch (Exception e) {
                log.error("Failed to send bulk push notification to user: {}", userId, e);
            }
        }
        
        log.info("Bulk push notification completed. Sent {}/{} notifications successfully", 
                successCount, userIds.size());
        return successCount;
    }
    
    @Override
    public int sendPushNotificationToRole(String role, String title, String message, Map<String, Object> data) {
        try {
            log.info("Sending push notification to role: {}", role);
            
            // Create notification payload
            Map<String, Object> notification = new HashMap<>();
            notification.put("title", title);
            notification.put("message", message);
            notification.put("timestamp", System.currentTimeMillis());
            notification.put("data", data);
            notification.put("role", role);
            
            // Send via WebSocket to all users with the role
            String destination = "/topic/role/" + role;
            messagingTemplate.convertAndSend(destination, notification);
            
            log.info("Push notification sent successfully to role: {}", role);
            
            // Return 1 as we don't track individual users for role-based notifications
            return 1;
            
        } catch (Exception e) {
            log.error("Failed to send push notification to role: {}", role, e);
            return 0;
        }
    }
    
    @Override
    public boolean registerDeviceToken(UUID userId, String deviceToken, String deviceType) {
        try {
            log.info("Registering device token for user: {}, type: {}", userId, deviceType);
            
            userDeviceTokens.computeIfAbsent(userId, k -> new HashMap<>())
                           .put(deviceType, deviceToken);
            
            log.info("Device token registered successfully for user: {}", userId);
            return true;
            
        } catch (Exception e) {
            log.error("Failed to register device token for user: {}", userId, e);
            return false;
        }
    }
    
    @Override
    public boolean unregisterDeviceToken(UUID userId, String deviceToken) {
        try {
            log.info("Unregistering device token for user: {}", userId);
            
            Map<String, String> userTokens = userDeviceTokens.get(userId);
            if (userTokens != null) {
                userTokens.entrySet().removeIf(entry -> entry.getValue().equals(deviceToken));
                
                if (userTokens.isEmpty()) {
                    userDeviceTokens.remove(userId);
                }
            }
            
            log.info("Device token unregistered successfully for user: {}", userId);
            return true;
            
        } catch (Exception e) {
            log.error("Failed to unregister device token for user: {}", userId, e);
            return false;
        }
    }
    
    @Override
    public boolean isServiceAvailable() {
        try {
            // Check if WebSocket messaging template is available
            return messagingTemplate != null;
        } catch (Exception e) {
            log.warn("Push notification service is not available: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Send notification to all connected users (broadcast)
     */
    public boolean sendBroadcastNotification(String title, String message, Map<String, Object> data) {
        try {
            log.info("Sending broadcast notification");
            
            Map<String, Object> notification = new HashMap<>();
            notification.put("title", title);
            notification.put("message", message);
            notification.put("timestamp", System.currentTimeMillis());
            notification.put("data", data);
            notification.put("type", "BROADCAST");
            
            messagingTemplate.convertAndSend("/topic/notifications", notification);
            
            log.info("Broadcast notification sent successfully");
            return true;
            
        } catch (Exception e) {
            log.error("Failed to send broadcast notification", e);
            return false;
        }
    }
    
    /**
     * Get registered device tokens for a user
     */
    public Map<String, String> getUserDeviceTokens(UUID userId) {
        return userDeviceTokens.getOrDefault(userId, new HashMap<>());
    }
    
    /**
     * Check if user has any registered device tokens
     */
    public boolean hasRegisteredDevices(UUID userId) {
        Map<String, String> tokens = userDeviceTokens.get(userId);
        return tokens != null && !tokens.isEmpty();
    }
}
