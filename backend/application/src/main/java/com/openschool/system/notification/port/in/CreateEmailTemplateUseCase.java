package com.openschool.system.notification.port.in;

import com.openschool.domain.notification.EmailTemplate;
import com.openschool.system.notification.port.in.command.CreateEmailTemplateCommand;

/**
 * Use case for creating email templates
 */
public interface CreateEmailTemplateUseCase {
    
    /**
     * Create a new email template
     * 
     * @param command the command containing template information
     * @return the created email template
     */
    EmailTemplate createEmailTemplate(CreateEmailTemplateCommand command);
}
