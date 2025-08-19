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
public class Exam {
    private UUID id;
    private String title;
    private String description;
    private String subject;
    private ExamType type;
    
    // Scheduling
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer durationMinutes;
    
    // Academic context
    private UUID schoolId;
    private UUID academicYearId;
    private UUID gradeId;
    private UUID classId;
    
    // Exam configuration
    private Integer totalMarks;
    private Integer passingMarks;
    private ExamStatus status;
    private String instructions;
    
    // Registration
    private LocalDateTime registrationStartDate;
    private LocalDateTime registrationEndDate;
    private Integer maxStudents;
    private Integer registeredStudents;
    
    // Supervision
    private UUID supervisorId;
    private String venue;
    private String roomNumber;
    
    // Results
    private boolean resultsPublished;
    private LocalDateTime resultsPublishedAt;
    
    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public boolean isRegistrationOpen() {
        LocalDateTime now = LocalDateTime.now();
        return status == ExamStatus.SCHEDULED &&
               registrationStartDate != null && registrationStartDate.isBefore(now) &&
               registrationEndDate != null && registrationEndDate.isAfter(now);
    }
    
    public boolean canRegisterStudent() {
        return isRegistrationOpen() && 
               (maxStudents == null || registeredStudents < maxStudents);
    }
    
    public boolean isInProgress() {
        LocalDateTime now = LocalDateTime.now();
        return status == ExamStatus.IN_PROGRESS &&
               startTime != null && startTime.isBefore(now) &&
               endTime != null && endTime.isAfter(now);
    }
    
    public boolean isCompleted() {
        return status == ExamStatus.COMPLETED ||
               (endTime != null && endTime.isBefore(LocalDateTime.now()));
    }
    
    public void registerStudent() {
        if (!canRegisterStudent()) {
            throw new IllegalStateException("Cannot register student for this exam");
        }
        this.registeredStudents++;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void unregisterStudent() {
        if (registeredStudents > 0) {
            this.registeredStudents--;
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void publishResults() {
        this.resultsPublished = true;
        this.resultsPublishedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
