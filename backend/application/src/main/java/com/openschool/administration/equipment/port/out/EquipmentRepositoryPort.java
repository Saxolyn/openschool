package com.openschool.administration.equipment.port.out;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.equipment.Equipment;

import java.util.Optional;
import java.util.UUID;

public interface EquipmentRepositoryPort {
    
    Equipment create(Equipment equipment);
    
    Equipment update(Equipment equipment);
    
    Optional<Equipment> findById(UUID equipmentId);
    
    Optional<Equipment> findByCode(String code);
    
    PageResult<Equipment> findAll(PageInfo pageInfo);
    
    PageResult<Equipment> findBySchoolId(UUID schoolId, PageInfo pageInfo);
    
    PageResult<Equipment> findByDepartmentId(UUID departmentId, PageInfo pageInfo);
    
    PageResult<Equipment> findByType(String type, PageInfo pageInfo);
    
    PageResult<Equipment> findByStatus(String status, PageInfo pageInfo);
    
    PageResult<Equipment> search(String searchTerm, PageInfo pageInfo);
    
    boolean delete(UUID equipmentId);
    
    boolean existsByCode(String code);
    
    boolean existsBySerialNumber(String serialNumber);
    
    long count();
    
    long countBySchoolId(UUID schoolId);
    
    long countByDepartmentId(UUID departmentId);
}
