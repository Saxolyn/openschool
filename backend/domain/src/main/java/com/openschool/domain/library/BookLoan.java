package com.openschool.domain.library;

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
public class BookLoan {
    private UUID id;
    private UUID bookId;
    private UUID borrowerId;
    private String borrowerType; // STUDENT, EMPLOYEE
    
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    
    private BookLoanStatus status;
    private String notes;
    
    // Renewal information
    private Integer renewalCount;
    private Integer maxRenewals;
    
    // Return condition
    private BookCondition returnCondition;
    private String returnNotes;
    
    // Fine information
    private Double fineAmount;
    private Boolean finePaid;
    
    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public boolean isOverdue() {
        return status == BookLoanStatus.BORROWED && 
               dueDate != null && 
               dueDate.isBefore(LocalDateTime.now());
    }
    
    public boolean canRenew() {
        return status == BookLoanStatus.BORROWED && 
               renewalCount < maxRenewals;
    }
    
    public void renewLoan(LocalDateTime newDueDate) {
        if (!canRenew()) {
            throw new IllegalStateException("Cannot renew this loan");
        }
        this.renewalCount++;
        this.dueDate = newDueDate;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void returnBook(BookCondition condition, String notes) {
        this.returnDate = LocalDateTime.now();
        this.returnCondition = condition;
        this.returnNotes = notes;
        this.status = BookLoanStatus.RETURNED;
        this.updatedAt = LocalDateTime.now();
    }
}
