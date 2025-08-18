package com.openschool.system.notification.port.in.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

/**
 * Command for creating email templates
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class CreateEmailTemplateCommand {
    
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
     * Whether this template should be active immediately
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
     * ID of the user creating this template
     */
    private UUID createdBy;
}
