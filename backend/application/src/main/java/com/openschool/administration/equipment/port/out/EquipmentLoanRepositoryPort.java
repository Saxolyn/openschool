package com.openschool.administration.equipment.port.out;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.equipment.EquipmentLoan;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EquipmentLoanRepositoryPort {
    
    EquipmentLoan create(EquipmentLoan loan);
    
    EquipmentLoan update(EquipmentLoan loan);
    
    Optional<EquipmentLoan> findById(UUID loanId);
    
    PageResult<EquipmentLoan> findAll(PageInfo pageInfo);
    
    PageResult<EquipmentLoan> findByEquipmentId(UUID equipmentId, PageInfo pageInfo);
    
    PageResult<EquipmentLoan> findByBorrowerId(UUID borrowerId, PageInfo pageInfo);
    
    PageResult<EquipmentLoan> findByStatus(String status, PageInfo pageInfo);
    
    List<EquipmentLoan> findOverdueLoans();
    
    List<EquipmentLoan> findActiveLoans();
    
    boolean delete(UUID loanId);
    
    long count();
    
    long countByEquipmentId(UUID equipmentId);
    
    long countByBorrowerId(UUID borrowerId);
}
