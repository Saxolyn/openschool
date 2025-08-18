package com.openschool.notification.port.in;

import com.openschool.notification.port.in.command.MarkAsReadCommand;

/**
 * Use case for marking notifications as read
 */
public interface MarkAsReadUseCase {
    
    /**
     * Mark a notification as read
     * 
     * @param command the command containing notification and user information
     * @return true if successfully marked as read
     */
    boolean markAsRead(MarkAsReadCommand command);
    
    /**
     * Mark multiple notifications as read
     * 
     * @param command the command containing notification IDs and user information
     * @return number of notifications marked as read
     */
    int markMultipleAsRead(MarkAsReadCommand command);
    
    /**
     * Mark all notifications as read for a user
     * 
     * @param command the command containing user information
     * @return number of notifications marked as read
     */
    int markAllAsRead(MarkAsReadCommand command);
}
