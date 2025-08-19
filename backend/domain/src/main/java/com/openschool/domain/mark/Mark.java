package com.openschool.domain.mark;

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
public class Mark {
    private UUID id;
    private UUID studentId;
    private UUID subjectId;
    private UUID examId;
    private UUID classId;
    
    // Mark details
    private String assessmentType; // EXAM, ASSIGNMENT, QUIZ, PROJECT
    private String assessmentName;
    private Double obtainedMarks;
    private Double totalMarks;
    private Double percentage;
    private String letterGrade;
    private Double gradePoint;
    
    // Academic context
    private UUID academicYearId;
    private UUID semesterId;
    private String term; // MIDTERM, FINAL, QUARTERLY
    
    // Status and approval
    private MarkStatus status;
    private UUID enteredById;
    private UUID approvedById;
    private LocalDateTime approvedAt;
    
    // Comments and feedback
    private String teacherComments;
    private String feedback;
    private String remarks;
    
    // Appeal process
    private boolean appealSubmitted;
    private String appealReason;
    private LocalDateTime appealSubmittedAt;
    private MarkAppealStatus appealStatus;
    private String appealResponse;
    private UUID appealReviewedById;
    
    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public boolean isPassing() {
        return percentage >= 50.0; // Configurable passing percentage
    }
    
    public boolean isApproved() {
        return status == MarkStatus.APPROVED;
    }
    
    public boolean canAppeal() {
        return isApproved() && !appealSubmitted;
    }
    
    public void approve(UUID approvedById) {
        this.status = MarkStatus.APPROVED;
        this.approvedById = approvedById;
        this.approvedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void reject(String reason) {
        this.status = MarkStatus.REJECTED;
        this.remarks = reason;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void submitAppeal(String reason) {
        if (!canAppeal()) {
            throw new IllegalStateException("Cannot submit appeal for this mark");
        }
        
        this.appealSubmitted = true;
        this.appealReason = reason;
        this.appealSubmittedAt = LocalDateTime.now();
        this.appealStatus = MarkAppealStatus.PENDING;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void processAppeal(MarkAppealStatus status, String response, UUID reviewedById) {
        this.appealStatus = status;
        this.appealResponse = response;
        this.appealReviewedById = reviewedById;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void calculateGrade() {
        this.percentage = (obtainedMarks / totalMarks) * 100;
        
        // Simple grading scale - can be made configurable
        if (percentage >= 90) {
            this.letterGrade = "A+";
            this.gradePoint = 4.0;
        } else if (percentage >= 80) {
            this.letterGrade = "A";
            this.gradePoint = 3.7;
        } else if (percentage >= 70) {
            this.letterGrade = "B";
            this.gradePoint = 3.0;
        } else if (percentage >= 60) {
            this.letterGrade = "C";
            this.gradePoint = 2.0;
        } else if (percentage >= 50) {
            this.letterGrade = "D";
            this.gradePoint = 1.0;
        } else {
            this.letterGrade = "F";
            this.gradePoint = 0.0;
        }
        
        this.updatedAt = LocalDateTime.now();
    }
}
