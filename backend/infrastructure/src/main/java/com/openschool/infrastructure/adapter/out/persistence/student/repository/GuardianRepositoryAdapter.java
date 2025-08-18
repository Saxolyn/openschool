package com.openschool.infrastructure.adapter.out.persistence.student.repository;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.student.Guardian;
import com.openschool.infrastructure.adapter.out.persistence.student.entity.GuardianEntity;
import com.openschool.infrastructure.adapter.out.persistence.student.repository.jpa.JpaGuardianRepository;
import com.openschool.infrastructure.adapter.out.persistence.student.repository.jpa.JpaStudentGuardianRepository;
import com.openschool.education.student.port.out.GuardianRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class GuardianRepositoryAdapter implements GuardianRepositoryPort {
    
    private final JpaGuardianRepository jpaGuardianRepository;
    private final JpaStudentGuardianRepository jpaStudentGuardianRepository;
    
    @Override
    public Guardian create(Guardian guardian) {
        GuardianEntity entity = GuardianEntity.fromDomain(guardian);
        GuardianEntity savedEntity = jpaGuardianRepository.save(entity);
        return savedEntity.toDomain();
    }
    
    @Override
    public Guardian update(Guardian guardian) {
        return create(guardian); // Same logic for update
    }
    
    @Override
    public Optional<Guardian> findById(UUID guardianId) {
        return jpaGuardianRepository.findById(guardianId)
                .map(GuardianEntity::toDomain);
    }
    
    @Override
    public Optional<Guardian> findByEmail(String email) {
        return jpaGuardianRepository.findByEmail(email)
                .map(GuardianEntity::toDomain);
    }
    
    @Override
    public Optional<Guardian> findByPhoneNumber(String phoneNumber) {
        return jpaGuardianRepository.findByPhoneNumber(phoneNumber)
                .map(GuardianEntity::toDomain);
    }
    
    @Override
    public List<Guardian> findByStudentId(UUID studentId) {
        List<UUID> guardianIds = jpaStudentGuardianRepository.findGuardianIdsByStudentId(studentId);
        return guardianIds.stream()
                .map(jpaGuardianRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(GuardianEntity::toDomain)
                .toList();
    }
    
    @Override
    public PageResult<Guardian> findAll(PageInfo pageInfo) {
        Pageable pageable = PageRequest.of(pageInfo.getPage(), pageInfo.getSize());
        Page<GuardianEntity> page = jpaGuardianRepository.findAll(pageable);
        
        List<Guardian> guardians = page.getContent()
                .stream()
                .map(GuardianEntity::toDomain)
                .toList();
        
        return new PageResult<>(guardians, page.getTotalElements(), page.getTotalPages());
    }
    
    @Override
    public PageResult<Guardian> search(String searchTerm, PageInfo pageInfo) {
        Pageable pageable = PageRequest.of(pageInfo.getPage(), pageInfo.getSize());
        Page<GuardianEntity> page = jpaGuardianRepository.search(searchTerm, pageable);
        
        List<Guardian> guardians = page.getContent()
                .stream()
                .map(GuardianEntity::toDomain)
                .toList();
        
        return new PageResult<>(guardians, page.getTotalElements(), page.getTotalPages());
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return jpaGuardianRepository.existsByEmail(email);
    }
    
    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return jpaGuardianRepository.existsByPhoneNumber(phoneNumber);
    }
    
    @Override
    public boolean delete(UUID guardianId) {
        if (jpaGuardianRepository.existsById(guardianId)) {
            jpaGuardianRepository.deleteById(guardianId);
            return true;
        }
        return false;
    }
}
