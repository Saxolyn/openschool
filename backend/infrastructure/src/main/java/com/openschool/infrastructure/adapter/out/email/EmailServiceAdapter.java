package com.openschool.infrastructure.adapter.out.email;

import com.openschool.notification.port.out.EmailServicePort;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Spring Mail implementation of EmailServicePort
 */
@Slf4j
@Service
@AllArgsConstructor
public class EmailServiceAdapter implements EmailServicePort {
    
    private final JavaMailSender mailSender;
    
    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    
    @Override
    public boolean sendEmail(String to, String subject, String content, boolean isHtml) {
        try {
            if (isHtml) {
                return sendHtmlEmail(to, subject, content);
            } else {
                return sendTextEmail(to, subject, content);
            }
        } catch (Exception e) {
            log.error("Failed to send email to: {}", to, e);
            return false;
        }
    }
    
    @Override
    public boolean sendEmail(String to, List<String> cc, List<String> bcc, 
                           String subject, String content, boolean isHtml) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, isHtml);
            
            if (cc != null && !cc.isEmpty()) {
                helper.setCc(cc.toArray(new String[0]));
            }
            
            if (bcc != null && !bcc.isEmpty()) {
                helper.setBcc(bcc.toArray(new String[0]));
            }
            
            mailSender.send(message);
            log.info("Email sent successfully to: {} (CC: {}, BCC: {})", to, cc, bcc);
            return true;
            
        } catch (MessagingException | MailException e) {
            log.error("Failed to send email to: {} (CC: {}, BCC: {})", to, cc, bcc, e);
            return false;
        }
    }
    
    @Override
    public boolean sendTemplatedEmail(String to, String templateId, Map<String, Object> variables) {
        // This is a simplified implementation
        // In a real application, you might integrate with a template engine like Thymeleaf
        // or use an external email service like SendGrid, AWS SES, etc.
        
        log.info("Sending templated email to: {} using template: {}", to, templateId);
        
        try {
            // For now, we'll just send a simple email indicating template usage
            String subject = "Notification from OpenSchool";
            String content = "This is a templated email. Template ID: " + templateId;
            
            if (variables != null && variables.containsKey("subject")) {
                subject = variables.get("subject").toString();
            }
            
            if (variables != null && variables.containsKey("content")) {
                content = variables.get("content").toString();
            }
            
            return sendEmail(to, subject, content, true);
            
        } catch (Exception e) {
            log.error("Failed to send templated email to: {} using template: {}", to, templateId, e);
            return false;
        }
    }
    
    @Override
    public int sendBulkEmail(List<String> recipients, String subject, String content, boolean isHtml) {
        if (recipients == null || recipients.isEmpty()) {
            return 0;
        }
        
        int successCount = 0;
        
        for (String recipient : recipients) {
            try {
                if (sendEmail(recipient, subject, content, isHtml)) {
                    successCount++;
                }
                
                // Add small delay to avoid overwhelming the mail server
                Thread.sleep(100);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("Bulk email sending interrupted");
                break;
            } catch (Exception e) {
                log.error("Failed to send bulk email to: {}", recipient, e);
            }
        }
        
        log.info("Bulk email completed. Sent {}/{} emails successfully", successCount, recipients.size());
        return successCount;
    }
    
    @Override
    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    @Override
    public boolean isServiceAvailable() {
        try {
            // Try to create a test message to check if the mail sender is configured
            mailSender.createMimeMessage();
            return true;
        } catch (Exception e) {
            log.warn("Email service is not available: {}", e.getMessage());
            return false;
        }
    }
    
    private boolean sendTextEmail(String to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            
            mailSender.send(message);
            log.info("Text email sent successfully to: {}", to);
            return true;
            
        } catch (MailException e) {
            log.error("Failed to send text email to: {}", to, e);
            return false;
        }
    }
    
    private boolean sendHtmlEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true); // true indicates HTML content
            
            mailSender.send(message);
            log.info("HTML email sent successfully to: {}", to);
            return true;
            
        } catch (MessagingException | MailException e) {
            log.error("Failed to send HTML email to: {}", to, e);
            return false;
        }
    }
}
