package com.openschool.domain.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Student {
    private UUID id;
    private String studentCode;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String email;
    private String phoneNumber;
    private String address;
    
    // Academic information
    private UUID schoolId;
    private UUID currentGradeId;
    private UUID currentClassId;
    private LocalDate enrollmentDate;
    private StudentStatus status;
    
    // Guardian relationships
    private List<UUID> guardianIds;
    
    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public boolean isActive() {
        return status == StudentStatus.ACTIVE;
    }
    
    public boolean canEnrollInClass() {
        return status == StudentStatus.ACTIVE && currentClassId == null;
    }
}
