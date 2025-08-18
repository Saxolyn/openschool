package com.openschool.domain.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Domain entity representing an email template
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class EmailTemplate {
    
    /**
     * Unique identifier for the template
     */
    private UUID id;
    
    /**
     * Name/identifier of the template
     */
    private String name;
    
    /**
     * Display name for the template
     */
    private String displayName;
    
    /**
     * Description of what this template is used for
     */
    private String description;
    
    /**
     * Subject line template (can contain variables)
     */
    private String subject;
    
    /**
     * HTML content of the email template
     */
    private String htmlContent;
    
    /**
     * Plain text content of the email template (fallback)
     */
    private String textContent;
    
    /**
     * List of variables that can be used in this template
     */
    private List<String> availableVariables;
    
    /**
     * Whether this template is active and can be used
     */
    private boolean active;
    
    /**
     * Category/group this template belongs to
     */
    private String category;
    
    /**
     * Language code for this template (e.g., "en", "vi")
     */
    private String language;
    
    /**
     * Version of the template for tracking changes
     */
    private Integer version;
    
    /**
     * When the template was created
     */
    private LocalDateTime createdAt;
    
    /**
     * When the template was last updated
     */
    private LocalDateTime updatedAt;
    
    /**
     * ID of the user who created this template
     */
    private UUID createdBy;
    
    /**
     * ID of the user who last updated this template
     */
    private UUID updatedBy;
    
    /**
     * Check if the template is active and can be used
     */
    public boolean isActive() {
        return active;
    }
    
    /**
     * Check if the template has HTML content
     */
    public boolean hasHtmlContent() {
        return htmlContent != null && !htmlContent.trim().isEmpty();
    }
    
    /**
     * Check if the template has text content
     */
    public boolean hasTextContent() {
        return textContent != null && !textContent.trim().isEmpty();
    }
    
    /**
     * Get the content to use based on preference (HTML or text)
     */
    public String getContentForType(boolean preferHtml) {
        if (preferHtml && hasHtmlContent()) {
            return htmlContent;
        } else if (hasTextContent()) {
            return textContent;
        } else if (hasHtmlContent()) {
            return htmlContent;
        }
        return "";
    }
    
    /**
     * Activate the template
     */
    public void activate() {
        this.active = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Deactivate the template
     */
    public void deactivate() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }
}
