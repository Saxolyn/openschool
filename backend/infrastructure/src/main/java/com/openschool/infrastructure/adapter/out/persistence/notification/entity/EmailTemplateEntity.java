package com.openschool.infrastructure.adapter.out.persistence.notification.entity;

import com.openschool.domain.notification.EmailTemplate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * JPA entity for email templates
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "email_templates", indexes = {
    @Index(name = "idx_email_template_name", columnList = "name", unique = true),
    @Index(name = "idx_email_template_category", columnList = "category"),
    @Index(name = "idx_email_template_language", columnList = "language"),
    @Index(name = "idx_email_template_active", columnList = "active")
})
public class EmailTemplateEntity {
    
    @Id
    private UUID id;
    
    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;
    
    @Column(name = "display_name", length = 200)
    private String displayName;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "subject", nullable = false, length = 500)
    private String subject;
    
    @Column(name = "html_content", columnDefinition = "TEXT")
    private String htmlContent;
    
    @Column(name = "text_content", columnDefinition = "TEXT")
    private String textContent;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "available_variables", columnDefinition = "jsonb")
    private List<String> availableVariables;
    
    @Column(name = "active", nullable = false)
    private boolean active;
    
    @Column(name = "category", length = 50)
    private String category;
    
    @Column(name = "language", length = 10)
    private String language;
    
    @Column(name = "version", nullable = false)
    private Integer version;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @Column(name = "created_by")
    private UUID createdBy;
    
    @Column(name = "updated_by")
    private UUID updatedBy;
    
    /**
     * Convert entity to domain object
     */
    public EmailTemplate toDomain() {
        return EmailTemplate.builder()
            .id(this.id)
            .name(this.name)
            .displayName(this.displayName)
            .description(this.description)
            .subject(this.subject)
            .htmlContent(this.htmlContent)
            .textContent(this.textContent)
            .availableVariables(this.availableVariables)
            .active(this.active)
            .category(this.category)
            .language(this.language)
            .version(this.version)
            .createdAt(this.createdAt)
            .updatedAt(this.updatedAt)
            .createdBy(this.createdBy)
            .updatedBy(this.updatedBy)
            .build();
    }
    
    /**
     * Create entity from domain object
     */
    public static EmailTemplateEntity fromDomain(EmailTemplate template) {
        return EmailTemplateEntity.builder()
            .id(template.getId())
            .name(template.getName())
            .displayName(template.getDisplayName())
            .description(template.getDescription())
            .subject(template.getSubject())
            .htmlContent(template.getHtmlContent())
            .textContent(template.getTextContent())
            .availableVariables(template.getAvailableVariables())
            .active(template.isActive())
            .category(template.getCategory())
            .language(template.getLanguage())
            .version(template.getVersion())
            .createdAt(template.getCreatedAt())
            .updatedAt(template.getUpdatedAt())
            .createdBy(template.getCreatedBy())
            .updatedBy(template.getUpdatedBy())
            .build();
    }
    
    /**
     * Create reference-only entity
     */
    public static EmailTemplateEntity referenceOnly(UUID id) {
        EmailTemplateEntity entity = new EmailTemplateEntity();
        entity.setId(id);
        return entity;
    }
}
