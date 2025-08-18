package com.openschool.education.grade.service;

import com.openschool.domain.grade.Grade;
import com.openschool.domain.school.School;
import com.openschool.education.grade.port.in.*;
import com.openschool.grade.port.in.*;
import com.openschool.education.grade.port.in.command.CreateGradeCommand;
import com.openschool.education.grade.port.in.command.UpdateGradeCommand;
import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.education.grade.port.out.GradeRepositoryPort;
import com.openschool.common.exception.DataNotFound;
import com.openschool.administration.school.port.out.SchoolRepositoryPort;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class GradeService implements
        CreateGradeUseCase,
        UpdateGradeUseCase,
        DeleteGradeUseCase,
        GetDetailGradeUseCase,
        GetListGradeUseCase {
    private final SchoolRepositoryPort schoolRepository;
    private final GradeRepositoryPort gradeRepository;

    @Override
    public Grade createGrade(CreateGradeCommand command, UUID schoolId) {
        Optional<School> school = schoolRepository.findById(schoolId);

        if(school.isEmpty()) {
            throw new DataNotFound("School not found with ID: " + schoolId);
        }

        Grade grade = Grade.builder()
                .schoolId(schoolId)
                .name(command.getName())
                .code(command.getCode())
                .level(command.getLevel())
                .minAge(command.getMinAge())
                .maxAge(command.getMaxAge())
                .displayOrder(command.getDisplayOrder())
                .allowClass(command.isAllowClass())
                .status(command.getStatus())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return gradeRepository.createGrade(grade);
    }

    @Override
    public Grade updateGrade(UpdateGradeCommand command, UUID gradeId) {
        Optional<Grade> gradeOpt = gradeRepository.findById(gradeId);
        if (gradeOpt.isEmpty()) {
            throw new DataNotFound("Grade not found with id: " + gradeId);
        }

        Grade grade = gradeOpt.get();

        // Update fields if provided
        if (command.getName() != null) {
            grade.setName(command.getName());
        }
        if (command.getCode() != null) {
            grade.setCode(command.getCode());
        }
        if (command.getLevel() != null) {
            grade.setLevel(command.getLevel());
        }
        if (command.getMinAge() != null) {
            grade.setMinAge(command.getMinAge());
        }
        if (command.getMaxAge() != null) {
            grade.setMaxAge(command.getMaxAge());
        }
        if (command.getDisplayOrder() != null) {
            grade.setDisplayOrder(command.getDisplayOrder());
        }
        if (command.getAllowClass() != null) {
            grade.setAllowClass(command.getAllowClass());
        }
        if (command.getStatus() != null) {
            grade.setStatus(command.getStatus());
        }

        grade.setUpdatedAt(LocalDateTime.now());
        return gradeRepository.update(grade);
    }

    @Override
    public void deleteGrade(UUID gradeId) {
        Optional<Grade> gradeOpt = gradeRepository.findById(gradeId);
        if (gradeOpt.isEmpty()) {
            throw new DataNotFound("Grade not found with id: " + gradeId);
        }

        gradeRepository.delete(gradeId);
    }

    @Override
    public Grade getDetailGrade(UUID gradeId) {
        Optional<Grade> gradeOpt = gradeRepository.findById(gradeId);
        if (gradeOpt.isEmpty()) {
            throw new DataNotFound("Grade not found with id: " + gradeId);
        }

        return gradeOpt.get();
    }

    @Override
    public PageResult<Grade> getListGrade(PageInfo pageInfo) {
        return gradeRepository.findAll(pageInfo);
    }
}
