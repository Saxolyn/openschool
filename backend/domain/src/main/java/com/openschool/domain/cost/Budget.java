package com.openschool.domain.cost;

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
public class Budget {
    private UUID id;
    private String name;
    private String description;
    private BudgetType type;
    private BudgetStatus status;
    
    // Financial details
    private Double totalAmount;
    private Double allocatedAmount;
    private Double spentAmount;
    private Double remainingAmount;
    private String currency;
    
    // Time period
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private String fiscalYear;
    
    // Assignment
    private UUID schoolId;
    private UUID departmentId;
    private UUID programId;
    private CostCategory category;
    
    // Approval
    private UUID approvedById;
    private LocalDateTime approvedAt;
    private String approvalNotes;
    
    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public boolean isActive() {
        return status == BudgetStatus.ACTIVE;
    }
    
    public boolean isOverspent() {
        return spentAmount != null && totalAmount != null && 
               spentAmount > totalAmount;
    }
    
    public Double getUtilizationPercentage() {
        if (totalAmount == null || totalAmount == 0) {
            return 0.0;
        }
        return (spentAmount / totalAmount) * 100;
    }
    
    public void allocate(Double amount) {
        if (amount > remainingAmount) {
            throw new IllegalArgumentException("Cannot allocate more than remaining budget");
        }
        this.allocatedAmount += amount;
        this.remainingAmount -= amount;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void spend(Double amount) {
        this.spentAmount += amount;
        this.remainingAmount = totalAmount - spentAmount;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void approve(UUID approvedById, String notes) {
        this.status = BudgetStatus.APPROVED;
        this.approvedById = approvedById;
        this.approvedAt = LocalDateTime.now();
        this.approvalNotes = notes;
        this.updatedAt = LocalDateTime.now();
    }
}
