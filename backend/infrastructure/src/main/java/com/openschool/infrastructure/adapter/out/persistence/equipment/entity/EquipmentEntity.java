package com.openschool.infrastructure.adapter.out.persistence.equipment.entity;

import com.openschool.domain.equipment.Equipment;
import com.openschool.domain.equipment.EquipmentCondition;
import com.openschool.domain.equipment.EquipmentStatus;
import com.openschool.domain.equipment.EquipmentType;
import com.openschool.infrastructure.adapter.out.persistence.common.BaseEntity;
import com.openschool.infrastructure.adapter.out.persistence.department.entity.DepartmentEntity;
import com.openschool.infrastructure.adapter.out.persistence.school.entity.SchoolEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "equipment")
public class EquipmentEntity extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String code;
    
    private String description;
    private String brand;
    private String model;
    
    @Column(unique = true)
    private String serialNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private SchoolEntity school;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;
    
    private String location;
    private UUID assignedToUserId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EquipmentType type;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EquipmentStatus status;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EquipmentCondition condition;
    
    private Double purchasePrice;
    private LocalDateTime purchaseDate;
    private String supplier;
    private String warrantyInfo;
    private LocalDateTime warrantyExpiry;
    
    @Column(nullable = false)
    private Integer quantity = 1;
    
    @Column(nullable = false)
    private Integer availableQuantity = 1;
    
    @Column(nullable = false)
    private Integer borrowedQuantity = 0;
    
    @Column(nullable = false)
    private Integer maintenanceQuantity = 0;
    
    private LocalDateTime lastMaintenanceDate;
    private LocalDateTime nextMaintenanceDate;
    private String maintenanceNotes;
    
    public static EquipmentEntity fromDomain(Equipment equipment, SchoolEntity school, DepartmentEntity department) {
        if (equipment == null) {
            return null;
        }
        return EquipmentEntity.builder()
                .id(equipment.getId())
                .name(equipment.getName())
                .code(equipment.getCode())
                .description(equipment.getDescription())
                .brand(equipment.getBrand())
                .model(equipment.getModel())
                .serialNumber(equipment.getSerialNumber())
                .school(school)
                .department(department)
                .location(equipment.getLocation())
                .assignedToUserId(equipment.getAssignedToUserId())
                .type(equipment.getType())
                .status(equipment.getStatus())
                .condition(equipment.getCondition())
                .purchasePrice(equipment.getPurchasePrice())
                .purchaseDate(equipment.getPurchaseDate())
                .supplier(equipment.getSupplier())
                .warrantyInfo(equipment.getWarrantyInfo())
                .warrantyExpiry(equipment.getWarrantyExpiry())
                .quantity(equipment.getQuantity())
                .availableQuantity(equipment.getAvailableQuantity())
                .borrowedQuantity(equipment.getBorrowedQuantity())
                .maintenanceQuantity(equipment.getMaintenanceQuantity())
                .lastMaintenanceDate(equipment.getLastMaintenanceDate())
                .nextMaintenanceDate(equipment.getNextMaintenanceDate())
                .maintenanceNotes(equipment.getMaintenanceNotes())
                .build();
    }
    
    public Equipment toDomain() {
        return Equipment.builder()
                .id(this.id)
                .name(this.name)
                .code(this.code)
                .description(this.description)
                .brand(this.brand)
                .model(this.model)
                .serialNumber(this.serialNumber)
                .schoolId(this.school != null ? this.school.getId() : null)
                .departmentId(this.department != null ? this.department.getDepartmentId() : null)
                .location(this.location)
                .assignedToUserId(this.assignedToUserId)
                .type(this.type)
                .status(this.status)
                .condition(this.condition)
                .purchasePrice(this.purchasePrice)
                .purchaseDate(this.purchaseDate)
                .supplier(this.supplier)
                .warrantyInfo(this.warrantyInfo)
                .warrantyExpiry(this.warrantyExpiry)
                .quantity(this.quantity)
                .availableQuantity(this.availableQuantity)
                .borrowedQuantity(this.borrowedQuantity)
                .maintenanceQuantity(this.maintenanceQuantity)
                .lastMaintenanceDate(this.lastMaintenanceDate)
                .nextMaintenanceDate(this.nextMaintenanceDate)
                .maintenanceNotes(this.maintenanceNotes)
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .build();
    }
    
    public static EquipmentEntity referenceOnly(UUID id) {
        EquipmentEntity entity = new EquipmentEntity();
        entity.setId(id);
        return entity;
    }
}
