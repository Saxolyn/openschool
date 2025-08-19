package com.openschool.domain.exam;

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
public class ExamResult {
    private UUID id;
    private UUID examId;
    private UUID studentId;
    
    // Scores
    private Double obtainedMarks;
    private Double totalMarks;
    private Double percentage;
    private String grade;
    private ExamResultStatus status;
    
    // Submission details
    private LocalDateTime submissionTime;
    private LocalDateTime startTime;
    private Integer timeTakenMinutes;
    
    // Grading
    private UUID gradedById;
    private LocalDateTime gradedAt;
    private String feedback;
    private String remarks;
    
    // Appeal process
    private boolean appealSubmitted;
    private String appealReason;
    private LocalDateTime appealSubmittedAt;
    private ExamAppealStatus appealStatus;
    private String appealResponse;
    
    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public boolean isPassed() {
        return status == ExamResultStatus.PASSED;
    }
    
    public boolean canAppeal() {
        return status == ExamResultStatus.FAILED && !appealSubmitted;
    }
    
    public void submitAppeal(String reason) {
        if (!canAppeal()) {
            throw new IllegalStateException("Cannot submit appeal for this result");
        }
        
        this.appealSubmitted = true;
        this.appealReason = reason;
        this.appealSubmittedAt = LocalDateTime.now();
        this.appealStatus = ExamAppealStatus.PENDING;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void processAppeal(ExamAppealStatus status, String response) {
        this.appealStatus = status;
        this.appealResponse = response;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void grade(UUID gradedById, Double marks, String grade, String feedback) {
        this.obtainedMarks = marks;
        this.percentage = (marks / totalMarks) * 100;
        this.grade = grade;
        this.feedback = feedback;
        this.gradedById = gradedById;
        this.gradedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
