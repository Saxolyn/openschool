package com.openschool.domain.equipment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Equipment {
    private UUID id;
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
    private UUID assignedToUserId;
    
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
    private Integer availableQuantity;
    private Integer borrowedQuantity;
    private Integer maintenanceQuantity;
    
    // Maintenance information
    private LocalDateTime lastMaintenanceDate;
    private LocalDateTime nextMaintenanceDate;
    private String maintenanceNotes;
    
    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public boolean isAvailableForBorrow() {
        return status == EquipmentStatus.ACTIVE && 
               condition == EquipmentCondition.GOOD && 
               availableQuantity > 0;
    }
    
    public boolean needsMaintenance() {
        return nextMaintenanceDate != null && 
               nextMaintenanceDate.isBefore(LocalDateTime.now());
    }
    
    public boolean isUnderWarranty() {
        return warrantyExpiry != null && 
               warrantyExpiry.isAfter(LocalDateTime.now());
    }
    
    public void borrowEquipment(int quantity) {
        if (quantity > availableQuantity) {
            throw new IllegalArgumentException("Not enough equipment available for borrowing");
        }
        this.availableQuantity -= quantity;
        this.borrowedQuantity += quantity;
    }
    
    public void returnEquipment(int quantity) {
        if (quantity > borrowedQuantity) {
            throw new IllegalArgumentException("Cannot return more equipment than borrowed");
        }
        this.borrowedQuantity -= quantity;
        this.availableQuantity += quantity;
    }
    
    public void addToInventory(int quantity) {
        this.quantity += quantity;
        this.availableQuantity += quantity;
    }
    
    public void removeFromInventory(int quantity) {
        if (quantity > availableQuantity) {
            throw new IllegalArgumentException("Cannot remove more equipment than available");
        }
        this.quantity -= quantity;
        this.availableQuantity -= quantity;
    }
}
