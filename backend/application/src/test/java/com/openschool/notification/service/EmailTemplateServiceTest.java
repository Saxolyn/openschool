package com.openschool.notification.service;

import com.openschool.domain.notification.EmailTemplate;
import com.openschool.system.notification.exception.NotificationException;
import com.openschool.system.notification.port.in.command.CreateEmailTemplateCommand;
import com.openschool.system.notification.port.out.EmailTemplateRepositoryPort;
import com.openschool.system.notification.service.EmailTemplateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for EmailTemplateService
 */
@ExtendWith(MockitoExtension.class)
class EmailTemplateServiceTest {
    
    @Mock
    private EmailTemplateRepositoryPort emailTemplateRepository;
    
    private EmailTemplateService emailTemplateService;
    
    @BeforeEach
    void setUp() {
        emailTemplateService = new EmailTemplateService(emailTemplateRepository);
    }
    
    @Test
    void createEmailTemplate_WithValidCommand_ShouldCreateSuccessfully() {
        // Given
        UUID createdBy = UUID.randomUUID();
        CreateEmailTemplateCommand command = CreateEmailTemplateCommand.builder()
            .name("welcome-email")
            .displayName("Welcome Email")
            .description("Welcome email for new users")
            .subject("Welcome to OpenSchool!")
            .htmlContent("<h1>Welcome {{userName}}!</h1>")
            .textContent("Welcome {{userName}}!")
            .availableVariables(Arrays.asList("userName", "schoolName"))
            .active(true)
            .category("USER_MANAGEMENT")
            .language("en")
            .createdBy(createdBy)
            .build();
        
        EmailTemplate savedTemplate = EmailTemplate.builder()
            .id(UUID.randomUUID())
            .name("welcome-email")
            .displayName("Welcome Email")
            .description("Welcome email for new users")
            .subject("Welcome to OpenSchool!")
            .htmlContent("<h1>Welcome {{userName}}!</h1>")
            .textContent("Welcome {{userName}}!")
            .availableVariables(Arrays.asList("userName", "schoolName"))
            .active(true)
            .category("USER_MANAGEMENT")
            .language("en")
            .version(1)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .createdBy(createdBy)
            .updatedBy(createdBy)
            .build();
        
        when(emailTemplateRepository.existsByName("welcome-email")).thenReturn(false);
        when(emailTemplateRepository.save(any(EmailTemplate.class))).thenReturn(savedTemplate);
        
        // When
        EmailTemplate result = emailTemplateService.createEmailTemplate(command);
        
        // Then
        assertNotNull(result);
        assertEquals("welcome-email", result.getName());
        assertEquals("Welcome Email", result.getDisplayName());
        assertEquals(1, result.getVersion());
        assertTrue(result.isActive());
        verify(emailTemplateRepository).existsByName("welcome-email");
        verify(emailTemplateRepository).save(any(EmailTemplate.class));
    }
    
    @Test
    void createEmailTemplate_WithExistingName_ShouldThrowException() {
        // Given
        CreateEmailTemplateCommand command = CreateEmailTemplateCommand.builder()
            .name("existing-template")
            .subject("Test Subject")
            .htmlContent("<p>Test</p>")
            .build();
        
        when(emailTemplateRepository.existsByName("existing-template")).thenReturn(true);
        
        // When & Then
        assertThrows(NotificationException.class, 
            () -> emailTemplateService.createEmailTemplate(command));
        verify(emailTemplateRepository, never()).save(any(EmailTemplate.class));
    }
    
    @Test
    void createEmailTemplate_WithMissingName_ShouldThrowException() {
        // Given
        CreateEmailTemplateCommand command = CreateEmailTemplateCommand.builder()
            .name(null)
            .subject("Test Subject")
            .htmlContent("<p>Test</p>")
            .build();
        
        // When & Then
        assertThrows(NotificationException.class, 
            () -> emailTemplateService.createEmailTemplate(command));
        verify(emailTemplateRepository, never()).save(any(EmailTemplate.class));
    }
    
    @Test
    void createEmailTemplate_WithMissingSubject_ShouldThrowException() {
        // Given
        CreateEmailTemplateCommand command = CreateEmailTemplateCommand.builder()
            .name("test-template")
            .subject(null)
            .htmlContent("<p>Test</p>")
            .build();
        
        when(emailTemplateRepository.existsByName("test-template")).thenReturn(false);
        
        // When & Then
        assertThrows(NotificationException.class, 
            () -> emailTemplateService.createEmailTemplate(command));
        verify(emailTemplateRepository, never()).save(any(EmailTemplate.class));
    }
    
    @Test
    void createEmailTemplate_WithMissingContent_ShouldThrowException() {
        // Given
        CreateEmailTemplateCommand command = CreateEmailTemplateCommand.builder()
            .name("test-template")
            .subject("Test Subject")
            .htmlContent(null)
            .textContent(null)
            .build();
        
        when(emailTemplateRepository.existsByName("test-template")).thenReturn(false);
        
        // When & Then
        assertThrows(NotificationException.class, 
            () -> emailTemplateService.createEmailTemplate(command));
        verify(emailTemplateRepository, never()).save(any(EmailTemplate.class));
    }
    
    @Test
    void updateEmailTemplate_WithValidData_ShouldUpdateSuccessfully() {
        // Given
        UUID templateId = UUID.randomUUID();
        UUID updatedBy = UUID.randomUUID();
        
        EmailTemplate existingTemplate = EmailTemplate.builder()
            .id(templateId)
            .name("old-name")
            .subject("Old Subject")
            .htmlContent("<p>Old content</p>")
            .version(1)
            .build();
        
        CreateEmailTemplateCommand command = CreateEmailTemplateCommand.builder()
            .name("new-name")
            .subject("New Subject")
            .htmlContent("<p>New content</p>")
            .createdBy(updatedBy)
            .build();
        
        EmailTemplate updatedTemplate = EmailTemplate.builder()
            .id(templateId)
            .name("new-name")
            .subject("New Subject")
            .htmlContent("<p>New content</p>")
            .version(2)
            .build();
        
        when(emailTemplateRepository.findById(templateId)).thenReturn(Optional.of(existingTemplate));
        when(emailTemplateRepository.existsByNameAndIdNot("new-name", templateId)).thenReturn(false);
        when(emailTemplateRepository.save(any(EmailTemplate.class))).thenReturn(updatedTemplate);
        
        // When
        EmailTemplate result = emailTemplateService.updateEmailTemplate(templateId, command);
        
        // Then
        assertNotNull(result);
        assertEquals("new-name", result.getName());
        assertEquals("New Subject", result.getSubject());
        assertEquals(2, result.getVersion());
        verify(emailTemplateRepository).save(any(EmailTemplate.class));
    }
    
    @Test
    void updateEmailTemplate_WithNonExistentTemplate_ShouldThrowException() {
        // Given
        UUID templateId = UUID.randomUUID();
        CreateEmailTemplateCommand command = CreateEmailTemplateCommand.builder()
            .name("test-template")
            .subject("Test Subject")
            .htmlContent("<p>Test</p>")
            .build();
        
        when(emailTemplateRepository.findById(templateId)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(NotificationException.class, 
            () -> emailTemplateService.updateEmailTemplate(templateId, command));
        verify(emailTemplateRepository, never()).save(any(EmailTemplate.class));
    }
    
    @Test
    void getEmailTemplate_WithExistingId_ShouldReturnTemplate() {
        // Given
        UUID templateId = UUID.randomUUID();
        EmailTemplate template = EmailTemplate.builder()
            .id(templateId)
            .name("test-template")
            .subject("Test Subject")
            .build();
        
        when(emailTemplateRepository.findById(templateId)).thenReturn(Optional.of(template));
        
        // When
        EmailTemplate result = emailTemplateService.getEmailTemplate(templateId);
        
        // Then
        assertNotNull(result);
        assertEquals(templateId, result.getId());
        assertEquals("test-template", result.getName());
        verify(emailTemplateRepository).findById(templateId);
    }
    
    @Test
    void getEmailTemplate_WithNonExistentId_ShouldThrowException() {
        // Given
        UUID templateId = UUID.randomUUID();
        when(emailTemplateRepository.findById(templateId)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(NotificationException.class, 
            () -> emailTemplateService.getEmailTemplate(templateId));
    }
    
    @Test
    void activateTemplate_WithExistingTemplate_ShouldActivateSuccessfully() {
        // Given
        UUID templateId = UUID.randomUUID();
        EmailTemplate template = EmailTemplate.builder()
            .id(templateId)
            .name("test-template")
            .active(false)
            .build();
        
        when(emailTemplateRepository.findById(templateId)).thenReturn(Optional.of(template));
        when(emailTemplateRepository.save(any(EmailTemplate.class))).thenReturn(template);
        
        // When
        boolean result = emailTemplateService.activateTemplate(templateId);
        
        // Then
        assertTrue(result);
        verify(emailTemplateRepository).save(any(EmailTemplate.class));
    }
    
    @Test
    void processTemplate_WithVariables_ShouldReplaceCorrectly() {
        // Given
        String template = "Hello {{userName}}, welcome to {{schoolName}}!";
        java.util.Map<String, Object> variables = java.util.Map.of(
            "userName", "John Doe",
            "schoolName", "OpenSchool Academy"
        );
        
        // When
        String result = emailTemplateService.processTemplate(template, variables);
        
        // Then
        assertEquals("Hello John Doe, welcome to OpenSchool Academy!", result);
    }
    
    @Test
    void processTemplate_WithNullTemplate_ShouldReturnNull() {
        // Given
        String template = null;
        java.util.Map<String, Object> variables = java.util.Map.of("key", "value");
        
        // When
        String result = emailTemplateService.processTemplate(template, variables);
        
        // Then
        assertNull(result);
    }
    
    @Test
    void validateTemplateVariables_WithAllRequiredVariables_ShouldReturnTrue() {
        // Given
        EmailTemplate template = EmailTemplate.builder()
            .availableVariables(Arrays.asList("userName", "schoolName"))
            .build();
        
        java.util.Map<String, Object> variables = java.util.Map.of(
            "userName", "John Doe",
            "schoolName", "OpenSchool Academy"
        );
        
        // When
        boolean result = emailTemplateService.validateTemplateVariables(template, variables);
        
        // Then
        assertTrue(result);
    }
    
    @Test
    void validateTemplateVariables_WithMissingVariables_ShouldReturnFalse() {
        // Given
        EmailTemplate template = EmailTemplate.builder()
            .availableVariables(Arrays.asList("userName", "schoolName"))
            .build();
        
        java.util.Map<String, Object> variables = java.util.Map.of("userName", "John Doe");
        
        // When
        boolean result = emailTemplateService.validateTemplateVariables(template, variables);
        
        // Then
        assertFalse(result);
    }
}
