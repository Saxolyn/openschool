package com.openschool.administration.equipment.port.in.command;

import com.openschool.domain.equipment.EquipmentCondition;
import com.openschool.domain.equipment.EquipmentStatus;
import com.openschool.domain.equipment.EquipmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateEquipmentCommand {
    private String name;
    private String code;
    private String description;
    private String brand;
    private String model;
    private String serialNumber;
    
    // Location and assignment
    private UUID schoolId;
    private UUID departmentId;
    private String location;
    
    // Equipment details
    private EquipmentType type;
    private EquipmentStatus status;
    private EquipmentCondition condition;
    
    // Financial information
    private Double purchasePrice;
    private LocalDateTime purchaseDate;
    private String supplier;
    private String warrantyInfo;
    private LocalDateTime warrantyExpiry;
    
    // Inventory management
    private Integer quantity;
}
