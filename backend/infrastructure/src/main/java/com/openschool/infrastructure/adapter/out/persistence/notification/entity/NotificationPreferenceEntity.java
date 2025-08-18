package com.openschool.infrastructure.adapter.out.persistence.notification.entity;

import com.openschool.domain.notification.NotificationPreference;
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
 * JPA entity for notification preferences
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notification_preferences", indexes = {
    @Index(name = "idx_notification_pref_user", columnList = "user_id", unique = true)
})
public class NotificationPreferenceEntity {
    
    @Id
    private UUID id;
    
    @Column(name = "user_id", nullable = false, unique = true)
    private UUID userId;
    
    @Column(name = "email_enabled", nullable = false)
    private boolean emailEnabled;
    
    @Column(name = "internal_message_enabled", nullable = false)
    private boolean internalMessageEnabled;
    
    @Column(name = "push_notification_enabled", nullable = false)
    private boolean pushNotificationEnabled;
    
    @Column(name = "sms_enabled", nullable = false)
    private boolean smsEnabled;
    
    @Column(name = "notification_email")
    private String notificationEmail;
    
    @Column(name = "notification_phone")
    private String notificationPhone;
    
    @Column(name = "preferred_language", length = 10)
    private String preferredLanguage;
    
    @Column(name = "timezone", length = 50)
    private String timezone;
    
    @Column(name = "digest_mode", nullable = false)
    private boolean digestMode;
    
    @Column(name = "digest_frequency", length = 20)
    private String digestFrequency;
    
    @Column(name = "digest_time", length = 5)
    private String digestTime;
    
    @Column(name = "quiet_hours_start", length = 5)
    private String quietHoursStart;
    
    @Column(name = "quiet_hours_end", length = 5)
    private String quietHoursEnd;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "category_preferences", columnDefinition = "jsonb")
    private Map<String, Map<String, Boolean>> categoryPreferences;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "custom_preferences", columnDefinition = "jsonb")
    private Map<String, Object> customPreferences;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    /**
     * Convert entity to domain object
     */
    public NotificationPreference toDomain() {
        return NotificationPreference.builder()
            .id(this.id)
            .userId(this.userId)
            .emailEnabled(this.emailEnabled)
            .internalMessageEnabled(this.internalMessageEnabled)
            .pushNotificationEnabled(this.pushNotificationEnabled)
            .smsEnabled(this.smsEnabled)
            .notificationEmail(this.notificationEmail)
            .notificationPhone(this.notificationPhone)
            .preferredLanguage(this.preferredLanguage)
            .timezone(this.timezone)
            .digestMode(this.digestMode)
            .digestFrequency(this.digestFrequency)
            .digestTime(this.digestTime)
            .quietHoursStart(this.quietHoursStart)
            .quietHoursEnd(this.quietHoursEnd)
            .categoryPreferences(this.categoryPreferences)
            .customPreferences(this.customPreferences)
            .createdAt(this.createdAt)
            .updatedAt(this.updatedAt)
            .build();
    }
    
    /**
     * Create entity from domain object
     */
    public static NotificationPreferenceEntity fromDomain(NotificationPreference preference) {
        return NotificationPreferenceEntity.builder()
            .id(preference.getId())
            .userId(preference.getUserId())
            .emailEnabled(preference.isEmailEnabled())
            .internalMessageEnabled(preference.isInternalMessageEnabled())
            .pushNotificationEnabled(preference.isPushNotificationEnabled())
            .smsEnabled(preference.isSmsEnabled())
            .notificationEmail(preference.getNotificationEmail())
            .notificationPhone(preference.getNotificationPhone())
            .preferredLanguage(preference.getPreferredLanguage())
            .timezone(preference.getTimezone())
            .digestMode(preference.isDigestMode())
            .digestFrequency(preference.getDigestFrequency())
            .digestTime(preference.getDigestTime())
            .quietHoursStart(preference.getQuietHoursStart())
            .quietHoursEnd(preference.getQuietHoursEnd())
            .categoryPreferences(preference.getCategoryPreferences())
            .customPreferences(preference.getCustomPreferences())
            .createdAt(preference.getCreatedAt())
            .updatedAt(preference.getUpdatedAt())
            .build();
    }
    
    /**
     * Create reference-only entity
     */
    public static NotificationPreferenceEntity referenceOnly(UUID id) {
        NotificationPreferenceEntity entity = new NotificationPreferenceEntity();
        entity.setId(id);
        return entity;
    }
}
