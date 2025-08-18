package com.openschool.administration.school.service;

import com.openschool.administration.school.port.in.*;
import com.openschool.administration.school.port.in.command.CreateSchoolCommand;
import com.openschool.administration.school.port.in.command.UpdateSchoolCommand;
import com.openschool.administration.school.port.out.SchoolRepositoryPort;
import com.openschool.common.exception.DataNotFound;
import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.school.School;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class SchoolService implements
        CreateSchoolUseCase,
        UpdateSchoolUseCase,
        DeleteSchoolUseCase,
        GetDetailSchoolUseCase,
        GetListSchoolUseCase {
    private final SchoolRepositoryPort schoolRepository;

    @Override
    public School create(CreateSchoolCommand command) {
        School school = School.builder()
                .id(UUID.randomUUID())
                .name(command.getName())
                .type(command.getType())
                .address(command.getAddress())
                .phoneNumber(command.getPhoneNumber())
                .email(command.getEmail())
                .website(command.getWebsite())
                .defaultLanguage(command.getDefaultLanguage())
                .timezone(command.getTimezone())
                .build();

        return schoolRepository.create(school);
    }

    @Override
    public void deleteSchool(UUID schoolId) {
        Optional<School> schoolOpt = schoolRepository.findById(schoolId);
        if (schoolOpt.isEmpty()) {
            throw new DataNotFound("School not found with id: " + schoolId);
        }

        // In a real implementation, you might want to check for dependencies
        // like students, classes, etc. before allowing deletion
        schoolRepository.delete(schoolId);
    }

    @Override
    public School getDetailSchool(UUID schoolId) {
        Optional<School> schoolOpt = schoolRepository.findById(schoolId);
        if (schoolOpt.isEmpty()) {
            throw new DataNotFound("School not found with id: " + schoolId);
        }

        return schoolOpt.get();
    }

    @Override
    public School update(UpdateSchoolCommand command) {
//        Optional<School> schoolOpt = schoolRepository.findById(command.getSchoolId());
//        if (schoolOpt.isEmpty()) {
//            throw new DataNotFound("School not found with id: " + command.getSchoolId());
//        }

//        School school = schoolOpt.get();

//        // Update fields if provided
//        if (command.getName() != null) {
//            school.setName(command.getName());
//        }
//        if (command.getType() != null) {
//            school.setType(command.getType());
//        }
//        if (command.getAddress() != null) {
//            school.setAddress(command.getAddress());
//        }
//        if (command.getPhoneNumber() != null) {
//            school.setPhoneNumber(command.getPhoneNumber());
//        }
//        if (command.getEmail() != null) {
//            school.setEmail(command.getEmail());
//        }
//        if (command.getWebsite() != null) {
//            school.setWebsite(command.getWebsite());
//        }
//        if (command.getDefaultLanguage() != null) {
//            school.setDefaultLanguage(command.getDefaultLanguage());
//        }
//        if (command.getTimezone() != null) {
//            school.setTimezone(command.getTimezone());
//        }
//
//        school.setUpdatedAt(LocalDateTime.now());
//        return schoolRepository.update(school);
        return null;
    }

    @Override
    public PageResult<School> getListSchool(PageInfo pageInfo) {
        return schoolRepository.findAll(pageInfo);
    }
}
