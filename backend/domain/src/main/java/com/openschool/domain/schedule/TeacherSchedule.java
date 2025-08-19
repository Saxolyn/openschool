package com.openschool.domain.schedule;

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
public class TeacherSchedule {
    private UUID id;
    private UUID teacherId;
    private UUID schoolId;
    
    // Schedule details
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private ScheduleType scheduleType;
    
    // Teaching assignment
    private UUID subjectId;
    private UUID classId;
    private UUID gradeId;
    private String room;
    
    // Availability
    private ScheduleStatus status;
    private boolean isAvailable;
    private String unavailabilityReason;
    
    // Validity period
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    
    // Workload tracking
    private Integer weeklyHours;
    private Integer maxWeeklyHours;
    
    // Notes
    private String notes;
    private String specialRequirements;
    
    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public boolean isActive() {
        return status == ScheduleStatus.ACTIVE;
    }
    
    public boolean isEffective(LocalDate date) {
        return isActive() && 
               (effectiveFrom == null || !date.isBefore(effectiveFrom)) &&
               (effectiveTo == null || !date.isAfter(effectiveTo));
    }
    
    public boolean hasTimeConflict(TeacherSchedule other) {
        if (!this.dayOfWeek.equals(other.dayOfWeek)) {
            return false;
        }
        
        return !(this.endTime.isBefore(other.startTime) || 
                this.startTime.isAfter(other.endTime));
    }
    
    public boolean isOverloaded() {
        return weeklyHours != null && maxWeeklyHours != null && 
               weeklyHours > maxWeeklyHours;
    }
    
    public void markUnavailable(String reason) {
        this.isAvailable = false;
        this.unavailabilityReason = reason;
        this.status = ScheduleStatus.UNAVAILABLE;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void markAvailable() {
        this.isAvailable = true;
        this.unavailabilityReason = null;
        this.status = ScheduleStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }
}
