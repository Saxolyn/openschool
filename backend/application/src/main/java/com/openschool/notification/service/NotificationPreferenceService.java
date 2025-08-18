package com.openschool.notification.service;

import com.openschool.domain.notification.NotificationPreference;
import com.openschool.notification.exception.ExceptionMessage;
import com.openschool.notification.exception.NotificationException;
import com.openschool.notification.port.in.UpdateNotificationPreferenceUseCase;
import com.openschool.notification.port.in.command.UpdateNotificationPreferenceCommand;
import com.openschool.notification.port.out.NotificationPreferenceRepositoryPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for managing notification preferences
 */
@Slf4j
@AllArgsConstructor
public class NotificationPreferenceService implements UpdateNotificationPreferenceUseCase {
    
    private final NotificationPreferenceRepositoryPort preferenceRepository;
    
    @Override
    public NotificationPreference updateNotificationPreference(UpdateNotificationPreferenceCommand command) {
        log.info("Updating notification preferences for user: {}", command.getUserId());
        
        if (command.getUserId() == null) {
            throw new NotificationException(ExceptionMessage.INVALID_PREFERENCE_DATA.getMessage() + ": User ID is required");
        }
        
        // Get existing preferences or create new ones
        NotificationPreference preference = preferenceRepository.findByUserId(command.getUserId())
            .orElse(createDefaultPreferences(command.getUserId()));
        
        // Update preferences with provided values
        updatePreferenceFields(preference, command);
        
        NotificationPreference savedPreference = preferenceRepository.save(preference);
        log.info("Notification preferences updated successfully for user: {}", command.getUserId());
        
        return savedPreference;
    }
    
    @Override
    public NotificationPreference getNotificationPreference(UUID userId) {
        return preferenceRepository.findByUserId(userId)
            .orElse(createDefaultPreferences(userId));
    }
    
    @Override
    public NotificationPreference createDefaultPreferences(UUID userId) {
        log.info("Creating default notification preferences for user: {}", userId);
        
        if (preferenceRepository.existsByUserId(userId)) {
            return preferenceRepository.findByUserId(userId).get();
        }
        
        // Create default category preferences
        Map<String, Map<String, Boolean>> defaultCategoryPreferences = createDefaultCategoryPreferences();
        
        NotificationPreference preference = NotificationPreference.builder()
            .id(UUID.randomUUID())
            .userId(userId)
            .emailEnabled(true)
            .internalMessageEnabled(true)
            .pushNotificationEnabled(true)
            .smsEnabled(false)
            .preferredLanguage("en")
            .timezone("UTC")
            .digestMode(false)
            .digestFrequency("DAILY")
            .digestTime("09:00")
            .categoryPreferences(defaultCategoryPreferences)
            .customPreferences(new HashMap<>())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        
        NotificationPreference savedPreference = preferenceRepository.save(preference);
        log.info("Default notification preferences created for user: {}", userId);
        
        return savedPreference;
    }
    
    /**
     * Enable specific notification type for user
     */
    public NotificationPreference enableNotificationType(UUID userId, String notificationType) {
        log.info("Enabling {} notifications for user: {}", notificationType, userId);
        
        NotificationPreference preference = getNotificationPreference(userId);
        
        switch (notificationType.toUpperCase()) {
            case "EMAIL":
                preference.setEmailEnabled(true);
                break;
            case "INTERNAL_MESSAGE":
                preference.setInternalMessageEnabled(true);
                break;
            case "PUSH_NOTIFICATION":
                preference.setPushNotificationEnabled(true);
                break;
            case "SMS":
                preference.setSmsEnabled(true);
                break;
            default:
                throw new NotificationException(ExceptionMessage.INVALID_NOTIFICATION_TYPE.getMessage());
        }
        
        preference.setUpdatedAt(LocalDateTime.now());
        return preferenceRepository.save(preference);
    }
    
    /**
     * Disable specific notification type for user
     */
    public NotificationPreference disableNotificationType(UUID userId, String notificationType) {
        log.info("Disabling {} notifications for user: {}", notificationType, userId);
        
        NotificationPreference preference = getNotificationPreference(userId);
        
        switch (notificationType.toUpperCase()) {
            case "EMAIL":
                preference.setEmailEnabled(false);
                break;
            case "INTERNAL_MESSAGE":
                preference.setInternalMessageEnabled(false);
                break;
            case "PUSH_NOTIFICATION":
                preference.setPushNotificationEnabled(false);
                break;
            case "SMS":
                preference.setSmsEnabled(false);
                break;
            default:
                throw new NotificationException(ExceptionMessage.INVALID_NOTIFICATION_TYPE.getMessage());
        }
        
        preference.setUpdatedAt(LocalDateTime.now());
        return preferenceRepository.save(preference);
    }
    
    /**
     * Update category-specific preferences
     */
    public NotificationPreference updateCategoryPreferences(UUID userId, String category, 
                                                           Map<String, Boolean> categorySettings) {
        log.info("Updating category preferences for user: {}, category: {}", userId, category);
        
        NotificationPreference preference = getNotificationPreference(userId);
        
        if (preference.getCategoryPreferences() == null) {
            preference.setCategoryPreferences(new HashMap<>());
        }
        
        preference.getCategoryPreferences().put(category, categorySettings);
        preference.setUpdatedAt(LocalDateTime.now());
        
        return preferenceRepository.save(preference);
    }
    
    /**
     * Set quiet hours for user
     */
    public NotificationPreference setQuietHours(UUID userId, String startTime, String endTime) {
        log.info("Setting quiet hours for user: {} from {} to {}", userId, startTime, endTime);
        
        // Validate time format (HH:mm)
        if (!isValidTimeFormat(startTime) || !isValidTimeFormat(endTime)) {
            throw new NotificationException(ExceptionMessage.INVALID_PREFERENCE_DATA.getMessage() + ": Invalid time format");
        }
        
        NotificationPreference preference = getNotificationPreference(userId);
        preference.setQuietHoursStart(startTime);
        preference.setQuietHoursEnd(endTime);
        preference.setUpdatedAt(LocalDateTime.now());
        
        return preferenceRepository.save(preference);
    }
    
    /**
     * Enable/disable digest mode
     */
    public NotificationPreference setDigestMode(UUID userId, boolean enabled, String frequency, String time) {
        log.info("Setting digest mode for user: {} - enabled: {}, frequency: {}, time: {}", 
                userId, enabled, frequency, time);
        
        NotificationPreference preference = getNotificationPreference(userId);
        preference.setDigestMode(enabled);
        
        if (enabled) {
            if (frequency != null) {
                preference.setDigestFrequency(frequency);
            }
            if (time != null && isValidTimeFormat(time)) {
                preference.setDigestTime(time);
            }
        }
        
        preference.setUpdatedAt(LocalDateTime.now());
        return preferenceRepository.save(preference);
    }
    
    private void updatePreferenceFields(NotificationPreference preference, UpdateNotificationPreferenceCommand command) {
        if (command.getEmailEnabled() != null) {
            preference.setEmailEnabled(command.getEmailEnabled());
        }
        if (command.getInternalMessageEnabled() != null) {
            preference.setInternalMessageEnabled(command.getInternalMessageEnabled());
        }
        if (command.getPushNotificationEnabled() != null) {
            preference.setPushNotificationEnabled(command.getPushNotificationEnabled());
        }
        if (command.getSmsEnabled() != null) {
            preference.setSmsEnabled(command.getSmsEnabled());
        }
        if (command.getNotificationEmail() != null) {
            preference.setNotificationEmail(command.getNotificationEmail());
        }
        if (command.getNotificationPhone() != null) {
            preference.setNotificationPhone(command.getNotificationPhone());
        }
        if (command.getPreferredLanguage() != null) {
            preference.setPreferredLanguage(command.getPreferredLanguage());
        }
        if (command.getTimezone() != null) {
            preference.setTimezone(command.getTimezone());
        }
        if (command.getDigestMode() != null) {
            preference.setDigestMode(command.getDigestMode());
        }
        if (command.getDigestFrequency() != null) {
            preference.setDigestFrequency(command.getDigestFrequency());
        }
        if (command.getDigestTime() != null) {
            preference.setDigestTime(command.getDigestTime());
        }
        if (command.getQuietHoursStart() != null) {
            preference.setQuietHoursStart(command.getQuietHoursStart());
        }
        if (command.getQuietHoursEnd() != null) {
            preference.setQuietHoursEnd(command.getQuietHoursEnd());
        }
        if (command.getCategoryPreferences() != null) {
            preference.setCategoryPreferences(command.getCategoryPreferences());
        }
        if (command.getCustomPreferences() != null) {
            preference.setCustomPreferences(command.getCustomPreferences());
        }
        
        preference.setUpdatedAt(LocalDateTime.now());
    }
    
    private Map<String, Map<String, Boolean>> createDefaultCategoryPreferences() {
        Map<String, Map<String, Boolean>> categoryPreferences = new HashMap<>();
        
        // Academic notifications
        Map<String, Boolean> academicSettings = new HashMap<>();
        academicSettings.put("EMAIL", true);
        academicSettings.put("INTERNAL_MESSAGE", true);
        academicSettings.put("PUSH_NOTIFICATION", true);
        categoryPreferences.put("ACADEMIC", academicSettings);
        
        // Administrative notifications
        Map<String, Boolean> adminSettings = new HashMap<>();
        adminSettings.put("EMAIL", true);
        adminSettings.put("INTERNAL_MESSAGE", true);
        adminSettings.put("PUSH_NOTIFICATION", false);
        categoryPreferences.put("ADMINISTRATIVE", adminSettings);
        
        // Emergency notifications
        Map<String, Boolean> emergencySettings = new HashMap<>();
        emergencySettings.put("EMAIL", true);
        emergencySettings.put("INTERNAL_MESSAGE", true);
        emergencySettings.put("PUSH_NOTIFICATION", true);
        categoryPreferences.put("EMERGENCY", emergencySettings);
        
        return categoryPreferences;
    }
    
    private boolean isValidTimeFormat(String time) {
        if (time == null) return false;
        return time.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$");
    }
}
