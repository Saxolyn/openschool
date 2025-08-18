package com.openschool.infrastructure.adapter.out.persistence.notification.repository;

import com.openschool.infrastructure.adapter.out.persistence.notification.entity.EmailTemplateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * JPA repository for email templates
 */
@Repository
public interface JpaEmailTemplateRepository extends JpaRepository<EmailTemplateEntity, UUID> {
    
    /**
     * Find template by name
     */
    Optional<EmailTemplateEntity> findByName(String name);
    
    /**
     * Find all active templates
     */
    List<EmailTemplateEntity> findByActiveTrue();
    
    /**
     * Find active templates with pagination
     */
    Page<EmailTemplateEntity> findByActiveTrue(Pageable pageable);
    
    /**
     * Find templates by category
     */
    List<EmailTemplateEntity> findByCategory(String category);
    
    /**
     * Find templates by language
     */
    List<EmailTemplateEntity> findByLanguage(String language);
    
    /**
     * Check if template name exists
     */
    boolean existsByName(String name);
    
    /**
     * Check if template name exists for a different template
     */
    boolean existsByNameAndIdNot(String name, UUID id);
    
    /**
     * Update template active status
     */
    @Modifying
    @Query("UPDATE EmailTemplateEntity e SET e.active = :active WHERE e.id = :id")
    int updateActiveStatus(@Param("id") UUID id, @Param("active") boolean active);
}
