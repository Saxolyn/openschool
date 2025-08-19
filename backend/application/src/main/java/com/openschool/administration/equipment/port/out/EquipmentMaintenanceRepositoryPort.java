package com.openschool.administration.equipment.port.out;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.equipment.EquipmentMaintenance;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EquipmentMaintenanceRepositoryPort {
    
    EquipmentMaintenance create(EquipmentMaintenance maintenance);
    
    EquipmentMaintenance update(EquipmentMaintenance maintenance);
    
    Optional<EquipmentMaintenance> findById(UUID maintenanceId);
    
    PageResult<EquipmentMaintenance> findAll(PageInfo pageInfo);
    
    PageResult<EquipmentMaintenance> findByEquipmentId(UUID equipmentId, PageInfo pageInfo);
    
    PageResult<EquipmentMaintenance> findByStatus(String status, PageInfo pageInfo);
    
    PageResult<EquipmentMaintenance> findByType(String type, PageInfo pageInfo);
    
    List<EquipmentMaintenance> findOverdueMaintenance();
    
    List<EquipmentMaintenance> findScheduledMaintenance();
    
    boolean delete(UUID maintenanceId);
    
    long count();
    
    long countByEquipmentId(UUID equipmentId);
}
