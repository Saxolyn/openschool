package com.openschool.infrastructure.adapter.out.persistence.student.repository.jpa;

import com.openschool.infrastructure.adapter.out.persistence.student.entity.GuardianEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaGuardianRepository extends JpaRepository<GuardianEntity, UUID> {
    
    Optional<GuardianEntity> findByEmail(String email);
    
    Optional<GuardianEntity> findByPhoneNumber(String phoneNumber);
    
    @Query("SELECT g FROM GuardianEntity g WHERE " +
           "LOWER(g.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(g.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(g.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(g.phoneNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<GuardianEntity> search(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    boolean existsByEmail(String email);
    
    boolean existsByPhoneNumber(String phoneNumber);
}
