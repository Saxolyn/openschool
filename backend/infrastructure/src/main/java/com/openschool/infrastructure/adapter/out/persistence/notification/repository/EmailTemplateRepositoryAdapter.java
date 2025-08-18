package com.openschool.infrastructure.adapter.out.persistence.notification.repository;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.notification.EmailTemplate;
import com.openschool.infrastructure.adapter.out.persistence.notification.entity.EmailTemplateEntity;
import com.openschool.system.notification.port.out.EmailTemplateRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * JPA implementation of EmailTemplateRepositoryPort
 */
@Repository
@AllArgsConstructor
public class EmailTemplateRepositoryAdapter implements EmailTemplateRepositoryPort {
    
    private final JpaEmailTemplateRepository jpaEmailTemplateRepository;
    
    @Override
    public EmailTemplate save(EmailTemplate template) {
        EmailTemplateEntity entity = EmailTemplateEntity.fromDomain(template);
        EmailTemplateEntity savedEntity = jpaEmailTemplateRepository.save(entity);
        return savedEntity.toDomain();
    }
    
    @Override
    public Optional<EmailTemplate> findById(UUID id) {
        return jpaEmailTemplateRepository.findById(id)
            .map(EmailTemplateEntity::toDomain);
    }
    
    @Override
    public Optional<EmailTemplate> findByName(String name) {
        return jpaEmailTemplateRepository.findByName(name)
            .map(EmailTemplateEntity::toDomain);
    }
    
    @Override
    public List<EmailTemplate> findAllActive() {
        return jpaEmailTemplateRepository.findByActiveTrue().stream()
            .map(EmailTemplateEntity::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<EmailTemplate> findByCategory(String category) {
        return jpaEmailTemplateRepository.findByCategory(category).stream()
            .map(EmailTemplateEntity::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<EmailTemplate> findByLanguage(String language) {
        return jpaEmailTemplateRepository.findByLanguage(language).stream()
            .map(EmailTemplateEntity::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public PageResult<EmailTemplate> findAll(PageInfo pageInfo) {
        Pageable pageable = PageRequest.of(pageInfo.getPage(), pageInfo.getSize());
        Page<EmailTemplateEntity> page = jpaEmailTemplateRepository.findAll(pageable);
        
        List<EmailTemplate> templates = page.getContent().stream()
            .map(EmailTemplateEntity::toDomain)
            .collect(Collectors.toList());
        
        return new PageResult<>(templates, page.getTotalElements(), page.getTotalPages(), page.getNumber());
    }
    
    @Override
    public PageResult<EmailTemplate> findAllActive(PageInfo pageInfo) {
        Pageable pageable = PageRequest.of(pageInfo.getPage(), pageInfo.getSize());
        Page<EmailTemplateEntity> page = jpaEmailTemplateRepository.findByActiveTrue(pageable);
        
        List<EmailTemplate> templates = page.getContent().stream()
            .map(EmailTemplateEntity::toDomain)
            .collect(Collectors.toList());
        
        return new PageResult<>(templates, page.getTotalElements(), page.getTotalPages(), page.getNumber());
    }
    
    @Override
    public boolean existsByName(String name) {
        return jpaEmailTemplateRepository.existsByName(name);
    }
    
    @Override
    public boolean existsByNameAndIdNot(String name, UUID excludeId) {
        return jpaEmailTemplateRepository.existsByNameAndIdNot(name, excludeId);
    }
    
    @Override
    @Transactional
    public boolean deleteById(UUID id) {
        if (jpaEmailTemplateRepository.existsById(id)) {
            jpaEmailTemplateRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    @Override
    @Transactional
    public boolean updateActiveStatus(UUID id, boolean active) {
        int updated = jpaEmailTemplateRepository.updateActiveStatus(id, active);
        return updated > 0;
    }
}
