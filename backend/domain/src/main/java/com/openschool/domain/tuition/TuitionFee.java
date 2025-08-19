package com.openschool.domain.tuition;

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
public class TuitionFee {
    private UUID id;
    private UUID studentId;
    private UUID academicYearId;
    private UUID gradeId;
    
    // Fee details
    private String feeType;
    private String description;
    private Double baseAmount;
    private Double discountAmount;
    private Double scholarshipAmount;
    private Double finalAmount;
    
    // Payment tracking
    private Double paidAmount;
    private Double outstandingAmount;
    private TuitionFeeStatus status;
    
    // Due dates
    private LocalDateTime dueDate;
    private LocalDateTime lastPaymentDate;
    
    // School assignment
    private UUID schoolId;
    
    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public boolean isOverdue() {
        return status != TuitionFeeStatus.PAID && 
               dueDate != null && 
               dueDate.isBefore(LocalDateTime.now());
    }
    
    public boolean isFullyPaid() {
        return status == TuitionFeeStatus.PAID && outstandingAmount <= 0;
    }
    
    public void applyPayment(Double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }
        
        if (amount > outstandingAmount) {
            throw new IllegalArgumentException("Payment amount exceeds outstanding amount");
        }
        
        this.paidAmount += amount;
        this.outstandingAmount -= amount;
        this.lastPaymentDate = LocalDateTime.now();
        
        if (outstandingAmount <= 0) {
            this.status = TuitionFeeStatus.PAID;
        } else {
            this.status = TuitionFeeStatus.PARTIALLY_PAID;
        }
        
        this.updatedAt = LocalDateTime.now();
    }
    
    public void applyDiscount(Double discountAmount) {
        if (discountAmount < 0) {
            throw new IllegalArgumentException("Discount amount cannot be negative");
        }
        
        this.discountAmount = discountAmount;
        this.finalAmount = baseAmount - discountAmount - scholarshipAmount;
        this.outstandingAmount = finalAmount - paidAmount;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void applyScholarship(Double scholarshipAmount) {
        if (scholarshipAmount < 0) {
            throw new IllegalArgumentException("Scholarship amount cannot be negative");
        }
        
        this.scholarshipAmount = scholarshipAmount;
        this.finalAmount = baseAmount - discountAmount - scholarshipAmount;
        this.outstandingAmount = finalAmount - paidAmount;
        this.updatedAt = LocalDateTime.now();
    }
}
