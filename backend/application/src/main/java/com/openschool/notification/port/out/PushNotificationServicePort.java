package com.openschool.notification.port.out;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Port for push notification service operations
 */
public interface PushNotificationServicePort {
    
    /**
     * Send a push notification to a specific user
     * 
     * @param userId the user ID
     * @param title notification title
     * @param message notification message
     * @param data additional data payload
     * @return true if notification was sent successfully
     */
    boolean sendPushNotification(UUID userId, String title, String message, Map<String, Object> data);
    
    /**
     * Send push notifications to multiple users
     * 
     * @param userIds list of user IDs
     * @param title notification title
     * @param message notification message
     * @param data additional data payload
     * @return number of notifications sent successfully
     */
    int sendBulkPushNotification(List<UUID> userIds, String title, String message, Map<String, Object> data);
    
    /**
     * Send push notification to all users in a role/group
     * 
     * @param role the role or group identifier
     * @param title notification title
     * @param message notification message
     * @param data additional data payload
     * @return number of notifications sent successfully
     */
    int sendPushNotificationToRole(String role, String title, String message, Map<String, Object> data);
    
    /**
     * Register a device token for push notifications
     * 
     * @param userId the user ID
     * @param deviceToken the device token
     * @param deviceType the device type (iOS, Android, Web)
     * @return true if token was registered successfully
     */
    boolean registerDeviceToken(UUID userId, String deviceToken, String deviceType);
    
    /**
     * Unregister a device token
     * 
     * @param userId the user ID
     * @param deviceToken the device token
     * @return true if token was unregistered successfully
     */
    boolean unregisterDeviceToken(UUID userId, String deviceToken);
    
    /**
     * Check if push notification service is available
     * 
     * @return true if service is available
     */
    boolean isServiceAvailable();
}
