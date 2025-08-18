package com.openschool.system.notification.port.in;

import com.openschool.domain.notification.Notification;
import com.openschool.system.notification.port.in.command.SendInternalMessageCommand;

import java.util.List;

/**
 * Use case for sending internal message notifications
 */
public interface SendInternalMessageUseCase {
    
    /**
     * Send an internal message notification
     * 
     * @param command the message command containing all necessary information
     * @return the created notification entity
     */
    Notification sendInternalMessage(SendInternalMessageCommand command);
    
    /**
     * Send internal message to multiple recipients
     * 
     * @param command the message command containing all necessary information
     * @return list of created notification entities
     */
    List<Notification> sendBulkInternalMessage(SendInternalMessageCommand command);
}
