package com.openschool.notification.port.in;

import com.openschool.domain.notification.Notification;
import com.openschool.notification.port.in.command.SendEmailCommand;

/**
 * Use case for sending email notifications
 */
public interface SendEmailUseCase {
    
    /**
     * Send an email notification
     * 
     * @param command the email command containing all necessary information
     * @return the created notification entity
     */
    Notification sendEmail(SendEmailCommand command);
}
