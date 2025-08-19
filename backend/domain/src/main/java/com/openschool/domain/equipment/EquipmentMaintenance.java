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
public class EquipmentMaintenance {
    private UUID id;
    private UUID equipmentId;
    
    private MaintenanceType type;
    private MaintenanceStatus status;
    private String description;
    private String notes;
    
    // Scheduling
    private LocalDateTime scheduledDate;
    private LocalDateTime startDate;
    private LocalDateTime completedDate;
    
    // Personnel
    private UUID assignedTechnicianId;
    private String externalServiceProvider;
    
    // Cost information
    private Double estimatedCost;
    private Double actualCost;
    
    // Parts and materials
    private String partsUsed;
    private String materialsUsed;
    
    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public boolean isOverdue() {
        return status != MaintenanceStatus.COMPLETED && 
               scheduledDate != null && 
               scheduledDate.isBefore(LocalDateTime.now());
    }
    
    public boolean isInProgress() {
        return status == MaintenanceStatus.IN_PROGRESS;
    }
    
    public void startMaintenance() {
        this.status = MaintenanceStatus.IN_PROGRESS;
        this.startDate = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void completeMaintenance(Double actualCost, String notes) {
        this.status = MaintenanceStatus.COMPLETED;
        this.completedDate = LocalDateTime.now();
        this.actualCost = actualCost;
        this.notes = notes;
        this.updatedAt = LocalDateTime.now();
    }
}
