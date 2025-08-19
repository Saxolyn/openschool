package com.openschool.administration.library.port.out;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.library.LibraryUser;

import java.util.Optional;
import java.util.UUID;

public interface LibraryUserRepositoryPort {
    
    LibraryUser create(LibraryUser user);
    
    LibraryUser update(LibraryUser user);
    
    Optional<LibraryUser> findById(UUID userId);
    
    Optional<LibraryUser> findByUserId(UUID userId);
    
    Optional<LibraryUser> findByLibraryCardNumber(String cardNumber);
    
    PageResult<LibraryUser> findAll(PageInfo pageInfo);
    
    PageResult<LibraryUser> findBySchoolId(UUID schoolId, PageInfo pageInfo);
    
    PageResult<LibraryUser> findByStatus(String status, PageInfo pageInfo);
    
    boolean delete(UUID userId);
    
    boolean existsByUserId(UUID userId);
    
    boolean existsByLibraryCardNumber(String cardNumber);
    
    long count();
    
    long countBySchoolId(UUID schoolId);
}
