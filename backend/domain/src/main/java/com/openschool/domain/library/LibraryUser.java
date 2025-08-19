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
public class LibraryUser {
    private UUID id;
    private UUID userId; // Reference to Student or Employee
    private String userType; // STUDENT, EMPLOYEE
    private String libraryCardNumber;
    
    // Borrowing limits
    private Integer maxBooksAllowed;
    private Integer currentBooksCount;
    private Integer maxRenewalCount;
    
    // Status
    private LibraryUserStatus status;
    private LocalDateTime registrationDate;
    private LocalDateTime expiryDate;
    
    // Fine information
    private Double totalFineAmount;
    private Double paidFineAmount;
    private Double outstandingFineAmount;
    
    // School assignment
    private UUID schoolId;
    private UUID libraryId;
    
    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public boolean canBorrowBooks() {
        return status == LibraryUserStatus.ACTIVE && 
               currentBooksCount < maxBooksAllowed &&
               outstandingFineAmount <= 0;
    }
    
    public boolean isExpired() {
        return expiryDate != null && expiryDate.isBefore(LocalDateTime.now());
    }
    
    public void borrowBook() {
        if (!canBorrowBooks()) {
            throw new IllegalStateException("User cannot borrow more books");
        }
        this.currentBooksCount++;
    }
    
    public void returnBook() {
        if (currentBooksCount <= 0) {
            throw new IllegalStateException("No books to return");
        }
        this.currentBooksCount--;
    }
    
    public void addFine(Double amount) {
        this.totalFineAmount += amount;
        this.outstandingFineAmount += amount;
    }
    
    public void payFine(Double amount) {
        if (amount > outstandingFineAmount) {
            throw new IllegalArgumentException("Payment amount exceeds outstanding fine");
        }
        this.paidFineAmount += amount;
        this.outstandingFineAmount -= amount;
    }
}
