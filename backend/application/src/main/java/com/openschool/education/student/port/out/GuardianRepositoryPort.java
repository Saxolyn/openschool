package com.openschool.education.student.port.out;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.student.Guardian;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GuardianRepositoryPort {
    
    Guardian create(Guardian guardian);
    
    Guardian update(Guardian guardian);
    
    Optional<Guardian> findById(UUID guardianId);
    
    Optional<Guardian> findByEmail(String email);
    
    Optional<Guardian> findByPhoneNumber(String phoneNumber);
    
    List<Guardian> findByStudentId(UUID studentId);
    
    PageResult<Guardian> findAll(PageInfo pageInfo);
    
    PageResult<Guardian> search(String searchTerm, PageInfo pageInfo);
    
    boolean existsByEmail(String email);
    
    boolean existsByPhoneNumber(String phoneNumber);
    
    boolean delete(UUID guardianId);
}
