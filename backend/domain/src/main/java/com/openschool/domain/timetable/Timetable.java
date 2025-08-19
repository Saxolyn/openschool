package com.openschool.domain.timetable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Timetable {
    private UUID id;
    private String name;
    private String description;
    
    // Academic context
    private UUID schoolId;
    private UUID academicYearId;
    private UUID gradeId;
    private UUID classId;
    
    // Schedule details
    private String dayOfWeek; // MONDAY, TUESDAY, etc.
    private LocalTime startTime;
    private LocalTime endTime;
    private String subject;
    private UUID teacherId;
    private String room;
    
    // Period information
    private Integer periodNumber;
    private Integer duration; // in minutes
    private TimetablePeriodType periodType;
    
    // Validity
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private TimetableStatus status;
    
    // Recurrence
    private boolean isRecurring;
    private String recurrencePattern; // WEEKLY, DAILY, etc.
    
    // Special notes
    private String notes;
    private String specialInstructions;
    
    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public boolean isActive() {
        return status == TimetableStatus.ACTIVE;
    }
    
    public boolean isEffective(LocalDate date) {
        return isActive() && 
               (effectiveFrom == null || !date.isBefore(effectiveFrom)) &&
               (effectiveTo == null || !date.isAfter(effectiveTo));
    }
    
    public boolean hasConflict(Timetable other) {
        if (!this.dayOfWeek.equals(other.dayOfWeek)) {
            return false;
        }
        
        return !(this.endTime.isBefore(other.startTime) || 
                this.startTime.isAfter(other.endTime));
    }
    
    public void activate() {
        this.status = TimetableStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void deactivate() {
        this.status = TimetableStatus.INACTIVE;
        this.updatedAt = LocalDateTime.now();
    }
}
