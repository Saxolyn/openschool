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
public class EquipmentLoan {
    private UUID id;
    private UUID equipmentId;
    private UUID borrowerId;
    private String borrowerType; // STUDENT, EMPLOYEE, DEPARTMENT
    
    private Integer quantity;
    private LocalDateTime borrowDate;
    private LocalDateTime expectedReturnDate;
    private LocalDateTime actualReturnDate;
    
    private EquipmentLoanStatus status;
    private String purpose;
    private String notes;
    
    // Approval workflow
    private UUID approvedById;
    private LocalDateTime approvedAt;
    
    // Return condition
    private EquipmentCondition returnCondition;
    private String returnNotes;
    
    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public boolean isOverdue() {
        return status == EquipmentLoanStatus.BORROWED && 
               expectedReturnDate != null && 
               expectedReturnDate.isBefore(LocalDateTime.now());
    }
    
    public boolean isActive() {
        return status == EquipmentLoanStatus.BORROWED;
    }
    
    public void returnEquipment(EquipmentCondition condition, String notes) {
        this.actualReturnDate = LocalDateTime.now();
        this.returnCondition = condition;
        this.returnNotes = notes;
        this.status = EquipmentLoanStatus.RETURNED;
        this.updatedAt = LocalDateTime.now();
    }
}
