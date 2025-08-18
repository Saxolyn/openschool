package com.openschool.domain.schoolclass.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SchoolClass {
    private UUID id;
    private String name;
    private String code;
    private String description;

    // Academic information
    private UUID schoolId;
    private UUID gradeId;
    private UUID academicYearId;

    // Capacity management
    private Integer maxStudents;
    private Integer currentStudentCount;

    // Staff assignments
    private UUID homeroomTeacherId;
    private List<UUID> subjectTeacherIds;

    // Status and lifecycle
    private ClassStatus status;

    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean canEnrollStudent() {
        return status == ClassStatus.ACTIVE &&
               (maxStudents == null || currentStudentCount < maxStudents);
    }

    public boolean isActive() {
        return status == ClassStatus.ACTIVE;
    }

    public boolean isFull() {
        return maxStudents != null && currentStudentCount >= maxStudents;
    }

    public void incrementStudentCount() {
        if (currentStudentCount == null) {
            currentStudentCount = 1;
        } else {
            currentStudentCount++;
        }
    }

    public void decrementStudentCount() {
        if (currentStudentCount != null && currentStudentCount > 0) {
            currentStudentCount--;
        }
    }
}
