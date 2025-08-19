package com.openschool.education.admissions.port.in.command;

import com.openschool.domain.admissions.AdmissionsPriority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAdmissionsApplicationCommand {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String email;
    private String phoneNumber;
    private String address;
    private UUID schoolId;
    private UUID gradeId;
    private UUID academicYearId;
    private String previousSchool;
    private String previousGrade;
    private String guardianName;
    private String guardianEmail;
    private String guardianPhone;
    private String guardianRelationship;
    private AdmissionsPriority priority;
    private boolean interviewRequired;
}
