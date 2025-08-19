package com.openschool.report.education.port.out;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.report.Report;

import java.util.Optional;
import java.util.UUID;

public interface ReportRepositoryPort {
    
    Report create(Report report);
    
    Report update(Report report);
    
    Optional<Report> findById(UUID reportId);
    
    PageResult<Report> findAll(PageInfo pageInfo);
    
    PageResult<Report> findByType(String type, PageInfo pageInfo);
    
    PageResult<Report> findByCategory(String category, PageInfo pageInfo);
    
    PageResult<Report> findBySchoolId(UUID schoolId, PageInfo pageInfo);
    
    PageResult<Report> findByGeneratedById(UUID generatedById, PageInfo pageInfo);
    
    boolean delete(UUID reportId);
    
    long count();
    
    long countByType(String type);
}
