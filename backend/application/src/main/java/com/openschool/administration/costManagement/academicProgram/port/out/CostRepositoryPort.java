package com.openschool.administration.costManagement.academicProgram.port.out;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.cost.Cost;
import com.openschool.domain.cost.CostCategory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CostRepositoryPort {
    
    Cost create(Cost cost);
    
    Cost update(Cost cost);
    
    Optional<Cost> findById(UUID costId);
    
    PageResult<Cost> findAll(PageInfo pageInfo);
    
    PageResult<Cost> findByCategory(CostCategory category, PageInfo pageInfo);
    
    PageResult<Cost> findByProgramId(UUID programId, PageInfo pageInfo);
    
    PageResult<Cost> findByDepartmentId(UUID departmentId, PageInfo pageInfo);

    PageResult<Cost> findByProjectId(UUID projectId, PageInfo pageInfo);

    PageResult<Cost> findByFiscalYear(String fiscalYear, PageInfo pageInfo);
    
    List<Cost> findByProgramIdAndFiscalYear(UUID programId, String fiscalYear);
    
    Double calculateTotalCostByProgram(UUID programId, String fiscalYear);
    
    Double calculateTotalCostByDepartment(UUID departmentId, String fiscalYear);

    Double calculateTotalCostBySchoolAndCategory(UUID schoolId, CostCategory category, String fiscalYear);

    boolean delete(UUID costId);

    long count();

    long countByCategory(CostCategory category);
}
