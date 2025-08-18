package com.openschool.notification.port.out;

import java.util.List;
import java.util.Map;

/**
 * Port for external email service operations
 */
public interface EmailServicePort {
    
    /**
     * Send a simple email
     * 
     * @param to recipient email address
     * @param subject email subject
     * @param content email content
     * @param isHtml whether content is HTML
     * @return true if email was sent successfully
     */
    boolean sendEmail(String to, String subject, String content, boolean isHtml);
    
    /**
     * Send an email with CC and BCC recipients
     * 
     * @param to primary recipient email address
     * @param cc list of CC email addresses
     * @param bcc list of BCC email addresses
     * @param subject email subject
     * @param content email content
     * @param isHtml whether content is HTML
     * @return true if email was sent successfully
     */
    boolean sendEmail(String to, List<String> cc, List<String> bcc, 
                     String subject, String content, boolean isHtml);
    
    /**
     * Send an email using a template
     * 
     * @param to recipient email address
     * @param templateId template identifier
     * @param variables template variables
     * @return true if email was sent successfully
     */
    boolean sendTemplatedEmail(String to, String templateId, Map<String, Object> variables);
    
    /**
     * Send bulk emails
     * 
     * @param recipients list of recipient email addresses
     * @param subject email subject
     * @param content email content
     * @param isHtml whether content is HTML
     * @return number of emails sent successfully
     */
    int sendBulkEmail(List<String> recipients, String subject, String content, boolean isHtml);
    
    /**
     * Validate email address format
     * 
     * @param email email address to validate
     * @return true if email format is valid
     */
    boolean isValidEmail(String email);
    
    /**
     * Check if email service is available
     * 
     * @return true if email service is available
     */
    boolean isServiceAvailable();
}
