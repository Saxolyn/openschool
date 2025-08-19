package com.openschool.education.admissions.service;

import com.openschool.education.admissions.port.in.*;
import com.openschool.education.admissions.port.in.command.*;
import com.openschool.education.admissions.port.out.*;
import com.openschool.domain.admissions.*;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public class AdmissionsService implements CreateAdmissionsApplicationUseCase {

    private final AdmissionsApplicationRepositoryPort admissionsRepository;

    @Override
    public AdmissionsApplication createAdmissionsApplication(CreateAdmissionsApplicationCommand command) {
        String applicationNumber = generateApplicationNumber();
        
        AdmissionsApplication application = AdmissionsApplication.builder()
                .id(UUID.randomUUID())
                .applicationNumber(applicationNumber)
                .firstName(command.getFirstName())
                .lastName(command.getLastName())
                .dateOfBirth(command.getDateOfBirth())
                .gender(command.getGender())
                .email(command.getEmail())
                .phoneNumber(command.getPhoneNumber())
                .address(command.getAddress())
                .schoolId(command.getSchoolId())
                .gradeId(command.getGradeId())
                .academicYearId(command.getAcademicYearId())
                .previousSchool(command.getPreviousSchool())
                .previousGrade(command.getPreviousGrade())
                .guardianName(command.getGuardianName())
                .guardianEmail(command.getGuardianEmail())
                .guardianPhone(command.getGuardianPhone())
                .guardianRelationship(command.getGuardianRelationship())
                .status(AdmissionsStatus.DRAFT)
                .priority(command.getPriority() != null ? command.getPriority() : AdmissionsPriority.NORMAL)
                .interviewRequired(command.isInterviewRequired())
                .documentsComplete(false)
                .onWaitlist(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return admissionsRepository.create(application);
    }

    private String generateApplicationNumber() {
        return "APP" + System.currentTimeMillis();
    }
}
