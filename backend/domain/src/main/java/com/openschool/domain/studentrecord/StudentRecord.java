package com.openschool.domain.studentrecord;

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
public class StudentRecord {
    private UUID id;
    private UUID studentId;
    private UUID academicYearId;
    private UUID gradeId;
    private UUID classId;
    
    // Academic performance
    private Double overallGPA;
    private String overallGrade;
    private Integer totalCredits;
    private Integer earnedCredits;
    
    // Attendance
    private Integer totalDaysPresent;
    private Integer totalDaysAbsent;
    private Double attendancePercentage;
    
    // Behavior and conduct
    private String conductGrade;
    private String behaviorNotes;
    private Integer disciplinaryActions;
    
    // Achievements
    private String achievements;
    private String awards;
    private String extracurricularActivities;
    
    // Teacher comments
    private String teacherComments;
    private String principalComments;
    
    // Status
    private StudentRecordStatus status;
    private boolean isPromoted;
    private String promotionNotes;
    
    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public boolean isEligibleForPromotion() {
        return overallGPA >= 2.0 && attendancePercentage >= 75.0;
    }
    
    public void promote(String notes) {
        this.isPromoted = true;
        this.promotionNotes = notes;
        this.status = StudentRecordStatus.PROMOTED;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateGPA(Double gpa) {
        this.overallGPA = gpa;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateAttendance(Integer present, Integer absent) {
        this.totalDaysPresent = present;
        this.totalDaysAbsent = absent;
        this.attendancePercentage = (double) present / (present + absent) * 100;
        this.updatedAt = LocalDateTime.now();
    }
}
