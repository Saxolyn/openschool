package com.openschool.infrastructure.config;

import com.openschool.system.notification.port.in.*;
import com.openschool.system.notification.port.out.*;
import com.openschool.system.notification.service.EmailTemplateService;
import com.openschool.system.notification.service.NotificationPreferenceService;
import com.openschool.system.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for notification system
 */
@Configuration
public class NotificationConfig {
    
    @Bean
    public NotificationService notificationService(
            NotificationRepositoryPort notificationRepository,
            EmailTemplateRepositoryPort emailTemplateRepository,
            NotificationPreferenceRepositoryPort preferenceRepository,
            EmailServicePort emailService,
            PushNotificationServicePort pushNotificationService) {
        
        return new NotificationService(
            notificationRepository,
            emailTemplateRepository,
            preferenceRepository,
            emailService,
            pushNotificationService
        );
    }
    
    @Bean
    public EmailTemplateService emailTemplateService(
            EmailTemplateRepositoryPort emailTemplateRepository) {
        
        return new EmailTemplateService(emailTemplateRepository);
    }
    
    @Bean
    public NotificationPreferenceService notificationPreferenceService(
            NotificationPreferenceRepositoryPort preferenceRepository) {
        
        return new NotificationPreferenceService(preferenceRepository);
    }
    
    // Use case beans
    
    @Bean
    @Qualifier("sendEmailUseCase")
    public SendEmailUseCase sendEmailUseCase(NotificationService notificationService) {
        return notificationService;
    }
    
    @Bean
    @Qualifier("sendInternalMessageUseCase")
    public SendInternalMessageUseCase sendInternalMessageUseCase(NotificationService notificationService) {
        return notificationService;
    }
    
    @Bean
    @Qualifier("getNotificationsUseCase")
    public GetNotificationsUseCase getNotificationsUseCase(NotificationService notificationService) {
        return notificationService;
    }
    
    @Bean
    @Qualifier("markAsReadUseCase")
    public MarkAsReadUseCase markAsReadUseCase(NotificationService notificationService) {
        return notificationService;
    }
    
    @Bean
    @Qualifier("getUnreadCountUseCase")
    public GetUnreadCountUseCase getUnreadCountUseCase(NotificationService notificationService) {
        return notificationService;
    }
    
    @Bean
    @Qualifier("createEmailTemplateUseCase")
    public CreateEmailTemplateUseCase createEmailTemplateUseCase(EmailTemplateService emailTemplateService) {
        return emailTemplateService;
    }
    
    @Bean
    @Qualifier("updateNotificationPreferenceUseCase")
    public UpdateNotificationPreferenceUseCase updateNotificationPreferenceUseCase(
            NotificationPreferenceService notificationPreferenceService) {
        return notificationPreferenceService;
    }
}
