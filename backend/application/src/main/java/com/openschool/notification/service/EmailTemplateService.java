package com.openschool.notification.service;

import com.openschool.domain.notification.EmailTemplate;
import com.openschool.notification.exception.ExceptionMessage;
import com.openschool.notification.exception.NotificationException;
import com.openschool.notification.port.in.CreateEmailTemplateUseCase;
import com.openschool.notification.port.in.command.CreateEmailTemplateCommand;
import com.openschool.notification.port.out.EmailTemplateRepositoryPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service for managing email templates
 */
@Slf4j
@AllArgsConstructor
public class EmailTemplateService implements CreateEmailTemplateUseCase {
    
    private final EmailTemplateRepositoryPort emailTemplateRepository;
    
    @Override
    public EmailTemplate createEmailTemplate(CreateEmailTemplateCommand command) {
        log.info("Creating email template: {}", command.getName());
        
        // Validate template name uniqueness
        if (emailTemplateRepository.existsByName(command.getName())) {
            throw new NotificationException(ExceptionMessage.EMAIL_TEMPLATE_NAME_EXISTS.getMessage());
        }
        
        // Validate required fields
        validateTemplateCommand(command);
        
        // Create template
        EmailTemplate template = EmailTemplate.builder()
            .id(UUID.randomUUID())
            .name(command.getName())
            .displayName(command.getDisplayName())
            .description(command.getDescription())
            .subject(command.getSubject())
            .htmlContent(command.getHtmlContent())
            .textContent(command.getTextContent())
            .availableVariables(command.getAvailableVariables())
            .active(command.isActive())
            .category(command.getCategory())
            .language(command.getLanguage() != null ? command.getLanguage() : "en")
            .version(1)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .createdBy(command.getCreatedBy())
            .updatedBy(command.getCreatedBy())
            .build();
        
        EmailTemplate savedTemplate = emailTemplateRepository.save(template);
        log.info("Email template created successfully: {}", savedTemplate.getName());
        
        return savedTemplate;
    }
    
    /**
     * Update an existing email template
     */
    public EmailTemplate updateEmailTemplate(UUID templateId, CreateEmailTemplateCommand command) {
        log.info("Updating email template: {}", templateId);
        
        EmailTemplate existingTemplate = emailTemplateRepository.findById(templateId)
            .orElseThrow(() -> new NotificationException(ExceptionMessage.EMAIL_TEMPLATE_NOT_FOUND.getMessage()));
        
        // Check name uniqueness (excluding current template)
        if (!existingTemplate.getName().equals(command.getName()) && 
            emailTemplateRepository.existsByNameAndIdNot(command.getName(), templateId)) {
            throw new NotificationException(ExceptionMessage.EMAIL_TEMPLATE_NAME_EXISTS.getMessage());
        }
        
        // Validate required fields
        validateTemplateCommand(command);
        
        // Update template
        existingTemplate.setName(command.getName());
        existingTemplate.setDisplayName(command.getDisplayName());
        existingTemplate.setDescription(command.getDescription());
        existingTemplate.setSubject(command.getSubject());
        existingTemplate.setHtmlContent(command.getHtmlContent());
        existingTemplate.setTextContent(command.getTextContent());
        existingTemplate.setAvailableVariables(command.getAvailableVariables());
        existingTemplate.setActive(command.isActive());
        existingTemplate.setCategory(command.getCategory());
        existingTemplate.setLanguage(command.getLanguage());
        existingTemplate.setVersion(existingTemplate.getVersion() + 1);
        existingTemplate.setUpdatedAt(LocalDateTime.now());
        existingTemplate.setUpdatedBy(command.getCreatedBy());
        
        EmailTemplate savedTemplate = emailTemplateRepository.save(existingTemplate);
        log.info("Email template updated successfully: {}", savedTemplate.getName());
        
        return savedTemplate;
    }
    
    /**
     * Get email template by ID
     */
    public EmailTemplate getEmailTemplate(UUID templateId) {
        return emailTemplateRepository.findById(templateId)
            .orElseThrow(() -> new NotificationException(ExceptionMessage.EMAIL_TEMPLATE_NOT_FOUND.getMessage()));
    }
    
    /**
     * Get email template by name
     */
    public EmailTemplate getEmailTemplateByName(String name) {
        return emailTemplateRepository.findByName(name)
            .orElseThrow(() -> new NotificationException(ExceptionMessage.EMAIL_TEMPLATE_NOT_FOUND.getMessage()));
    }
    
    /**
     * Activate email template
     */
    public boolean activateTemplate(UUID templateId) {
        log.info("Activating email template: {}", templateId);
        
        EmailTemplate template = getEmailTemplate(templateId);
        template.activate();
        emailTemplateRepository.save(template);
        
        return true;
    }
    
    /**
     * Deactivate email template
     */
    public boolean deactivateTemplate(UUID templateId) {
        log.info("Deactivating email template: {}", templateId);
        
        EmailTemplate template = getEmailTemplate(templateId);
        template.deactivate();
        emailTemplateRepository.save(template);
        
        return true;
    }
    
    /**
     * Delete email template
     */
    public boolean deleteTemplate(UUID templateId) {
        log.info("Deleting email template: {}", templateId);
        
        if (!emailTemplateRepository.findById(templateId).isPresent()) {
            throw new NotificationException(ExceptionMessage.EMAIL_TEMPLATE_NOT_FOUND.getMessage());
        }
        
        return emailTemplateRepository.deleteById(templateId);
    }
    
    /**
     * Process template with variables
     */
    public String processTemplate(String templateContent, java.util.Map<String, Object> variables) {
        if (templateContent == null || variables == null) {
            return templateContent;
        }
        
        String result = templateContent;
        for (java.util.Map.Entry<String, Object> entry : variables.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            String value = entry.getValue() != null ? entry.getValue().toString() : "";
            result = result.replace(placeholder, value);
        }
        
        return result;
    }
    
    /**
     * Validate template variables in content
     */
    public boolean validateTemplateVariables(EmailTemplate template, java.util.Map<String, Object> variables) {
        if (template.getAvailableVariables() == null || template.getAvailableVariables().isEmpty()) {
            return true;
        }
        
        // Check if all required variables are provided
        for (String requiredVar : template.getAvailableVariables()) {
            if (variables == null || !variables.containsKey(requiredVar)) {
                log.warn("Missing required template variable: {}", requiredVar);
                return false;
            }
        }
        
        return true;
    }
    
    private void validateTemplateCommand(CreateEmailTemplateCommand command) {
        if (command.getName() == null || command.getName().trim().isEmpty()) {
            throw new NotificationException(ExceptionMessage.INVALID_TEMPLATE_DATA.getMessage() + ": Name is required");
        }
        
        if (command.getSubject() == null || command.getSubject().trim().isEmpty()) {
            throw new NotificationException(ExceptionMessage.INVALID_TEMPLATE_DATA.getMessage() + ": Subject is required");
        }
        
        if ((command.getHtmlContent() == null || command.getHtmlContent().trim().isEmpty()) &&
            (command.getTextContent() == null || command.getTextContent().trim().isEmpty())) {
            throw new NotificationException(ExceptionMessage.INVALID_TEMPLATE_DATA.getMessage() + ": At least one content type is required");
        }
    }
}
