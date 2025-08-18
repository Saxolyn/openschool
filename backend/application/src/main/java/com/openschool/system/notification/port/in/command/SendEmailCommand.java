package com.openschool.system.notification.port.in.command;

import com.openschool.domain.notification.NotificationPriority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Command for sending email notifications
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class SendEmailCommand {
    
    /**
     * Email address to send to
     */
    private String to;
    
    /**
     * List of CC email addresses
     */
    private List<String> cc;
    
    /**
     * List of BCC email addresses
     */
    private List<String> bcc;
    
    /**
     * Email subject
     */
    private String subject;
    
    /**
     * Email content (HTML or plain text)
     */
    private String content;
    
    /**
     * Whether the content is HTML
     */
    private boolean isHtml;
    
    /**
     * Template ID to use (optional)
     */
    private UUID templateId;
    
    /**
     * Variables to populate the template
     */
    private Map<String, Object> templateVariables;
    
    /**
     * Priority of the email
     */
    private NotificationPriority priority;
    
    /**
     * ID of the user sending the email (optional for system emails)
     */
    private UUID senderId;
    
    /**
     * ID of the recipient user (if known)
     */
    private UUID recipientId;
    
    /**
     * Category for the notification (e.g., "ACADEMIC", "ADMINISTRATIVE")
     */
    private String category;
    
    /**
     * Additional metadata
     */
    private Map<String, Object> metadata;
    
    /**
     * Whether to send immediately or queue for later
     */
    private boolean sendImmediately;
    
    /**
     * Maximum number of retry attempts if sending fails
     */
    private Integer maxRetries;
}
