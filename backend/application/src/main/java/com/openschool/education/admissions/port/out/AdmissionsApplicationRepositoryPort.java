package com.openschool.education.admissions.port.out;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.admissions.AdmissionsApplication;

import java.util.Optional;
import java.util.UUID;

public interface AdmissionsApplicationRepositoryPort {
    
    AdmissionsApplication create(AdmissionsApplication application);
    
    AdmissionsApplication update(AdmissionsApplication application);
    
    Optional<AdmissionsApplication> findById(UUID applicationId);
    
    Optional<AdmissionsApplication> findByApplicationNumber(String applicationNumber);
    
    PageResult<AdmissionsApplication> findAll(PageInfo pageInfo);
    
    PageResult<AdmissionsApplication> findBySchoolId(UUID schoolId, PageInfo pageInfo);
    
    PageResult<AdmissionsApplication> findByStatus(String status, PageInfo pageInfo);
    
    PageResult<AdmissionsApplication> findByGradeId(UUID gradeId, PageInfo pageInfo);
    
    boolean delete(UUID applicationId);
    
    long count();
    
    long countByStatus(String status);
}
