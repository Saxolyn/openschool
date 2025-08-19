package com.openschool.administration.costManagement.academicProgram.port.out;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.cost.Budget;
import com.openschool.domain.cost.BudgetType;
import com.openschool.domain.cost.CostCategory;

import java.util.Optional;
import java.util.UUID;

public interface BudgetRepositoryPort {
    
    Budget create(Budget budget);
    
    Budget update(Budget budget);
    
    Optional<Budget> findById(UUID budgetId);
    
    PageResult<Budget> findAll(PageInfo pageInfo);
    
    PageResult<Budget> findByType(BudgetType type, PageInfo pageInfo);
    
    PageResult<Budget> findByCategory(CostCategory category, PageInfo pageInfo);
    
    PageResult<Budget> findByProgramId(UUID programId, PageInfo pageInfo);
    
    PageResult<Budget> findByDepartmentId(UUID departmentId, PageInfo pageInfo);
    
    PageResult<Budget> findByFiscalYear(String fiscalYear, PageInfo pageInfo);
    
    Optional<Budget> findByProgramIdAndFiscalYear(UUID programId, String fiscalYear);
    
    boolean delete(UUID budgetId);
    
    long count();
    
    Double getTotalBudgetByFiscalYear(String fiscalYear);
}
