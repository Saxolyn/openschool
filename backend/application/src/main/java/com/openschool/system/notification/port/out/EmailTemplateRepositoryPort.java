package com.openschool.system.notification.port.out;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.notification.EmailTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository port for email template persistence operations
 */
public interface EmailTemplateRepositoryPort {
    
    /**
     * Save an email template
     * 
     * @param template the template to save
     * @return the saved template
     */
    EmailTemplate save(EmailTemplate template);
    
    /**
     * Find a template by ID
     * 
     * @param id the template ID
     * @return the template if found
     */
    Optional<EmailTemplate> findById(UUID id);
    
    /**
     * Find a template by name
     * 
     * @param name the template name
     * @return the template if found
     */
    Optional<EmailTemplate> findByName(String name);
    
    /**
     * Find all active templates
     * 
     * @return list of active templates
     */
    List<EmailTemplate> findAllActive();
    
    /**
     * Find templates by category
     * 
     * @param category the template category
     * @return list of templates in the category
     */
    List<EmailTemplate> findByCategory(String category);
    
    /**
     * Find templates by language
     * 
     * @param language the language code
     * @return list of templates for the language
     */
    List<EmailTemplate> findByLanguage(String language);
    
    /**
     * Find templates with pagination
     * 
     * @param pageInfo pagination information
     * @return paginated templates
     */
    PageResult<EmailTemplate> findAll(PageInfo pageInfo);
    
    /**
     * Find active templates with pagination
     * 
     * @param pageInfo pagination information
     * @return paginated active templates
     */
    PageResult<EmailTemplate> findAllActive(PageInfo pageInfo);
    
    /**
     * Check if template name exists
     * 
     * @param name the template name
     * @return true if name exists
     */
    boolean existsByName(String name);
    
    /**
     * Check if template name exists for a different template
     * 
     * @param name the template name
     * @param excludeId template ID to exclude from check
     * @return true if name exists for a different template
     */
    boolean existsByNameAndIdNot(String name, UUID excludeId);
    
    /**
     * Delete a template
     * 
     * @param id the template ID
     * @return true if successfully deleted
     */
    boolean deleteById(UUID id);
    
    /**
     * Update template status (active/inactive)
     * 
     * @param id the template ID
     * @param active the new active status
     * @return true if successfully updated
     */
    boolean updateActiveStatus(UUID id, boolean active);
}
