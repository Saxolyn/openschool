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
public class Book {
    private UUID id;
    private String title;
    private String isbn;
    private String author;
    private String publisher;
    private String category;
    private String description;
    
    // Physical details
    private Integer totalCopies;
    private Integer availableCopies;
    private Integer borrowedCopies;
    private String location;
    private String shelfNumber;
    
    // Book details
    private Integer publicationYear;
    private String language;
    private Integer pages;
    private BookStatus status;
    private BookCondition condition;
    
    // School assignment
    private UUID schoolId;
    private UUID libraryId;
    
    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public boolean isAvailableForBorrow() {
        return status == BookStatus.ACTIVE && availableCopies > 0;
    }
    
    public void borrowBook() {
        if (availableCopies <= 0) {
            throw new IllegalStateException("No copies available for borrowing");
        }
        this.availableCopies--;
        this.borrowedCopies++;
    }
    
    public void returnBook() {
        if (borrowedCopies <= 0) {
            throw new IllegalStateException("No borrowed copies to return");
        }
        this.borrowedCopies--;
        this.availableCopies++;
    }
    
    public void addCopies(int copies) {
        this.totalCopies += copies;
        this.availableCopies += copies;
    }
    
    public void removeCopies(int copies) {
        if (copies > availableCopies) {
            throw new IllegalArgumentException("Cannot remove more copies than available");
        }
        this.totalCopies -= copies;
        this.availableCopies -= copies;
    }
}
