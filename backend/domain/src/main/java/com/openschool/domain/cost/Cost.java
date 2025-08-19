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
public class Cost {
    private UUID id;
    private String name;
    private String description;
    private CostCategory category;
    private CostType type;
    
    // Financial details
    private Double amount;
    private String currency;
    private CostStatus status;
    
    // Budget tracking
    private UUID budgetId;
    private Double budgetedAmount;
    private Double actualAmount;
    private Double variance;
    
    // Time period
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private String fiscalYear;
    private String quarter;
    private String month;
    
    // Assignment
    private UUID schoolId;
    private UUID departmentId;
    private UUID programId;
    private UUID projectId;
    
    // Approval workflow
    private CostApprovalStatus approvalStatus;
    private UUID approvedById;
    private LocalDateTime approvedAt;
    private String approvalNotes;
    
    // Vendor information
    private UUID vendorId;
    private String vendorName;
    private String invoiceNumber;
    private LocalDateTime invoiceDate;
    private LocalDateTime dueDate;
    
    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public boolean isOverBudget() {
        return budgetedAmount != null && actualAmount != null && 
               actualAmount > budgetedAmount;
    }
    
    public boolean isApproved() {
        return approvalStatus == CostApprovalStatus.APPROVED;
    }
    
    public boolean isPending() {
        return approvalStatus == CostApprovalStatus.PENDING;
    }
    
    public void approve(UUID approvedById, String notes) {
        this.approvalStatus = CostApprovalStatus.APPROVED;
        this.approvedById = approvedById;
        this.approvedAt = LocalDateTime.now();
        this.approvalNotes = notes;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void reject(String reason) {
        this.approvalStatus = CostApprovalStatus.REJECTED;
        this.approvalNotes = reason;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void calculateVariance() {
        if (budgetedAmount != null && actualAmount != null) {
            this.variance = actualAmount - budgetedAmount;
        }
        this.updatedAt = LocalDateTime.now();
    }
    
    public Double getVariancePercentage() {
        if (budgetedAmount == null || budgetedAmount == 0) {
            return null;
        }
        return (variance / budgetedAmount) * 100;
    }
}
