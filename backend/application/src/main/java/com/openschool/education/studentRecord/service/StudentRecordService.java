package com.openschool.education.studentRecord.service;

import com.openschool.education.studentRecord.port.in.*;
import com.openschool.education.studentRecord.port.in.command.*;
import com.openschool.education.studentRecord.port.out.*;
import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.studentrecord.*;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public class StudentRecordService implements
        CreateStudentRecordUseCase,
        UpdateStudentRecordUseCase,
        ViewStudentRecordUseCase,
        DeleteStudentRecordUseCase {

    private final StudentRecordRepositoryPort studentRecordRepository;

    @Override
    public StudentRecord createStudentRecord(CreateStudentRecordCommand command) {
        StudentRecord record = StudentRecord.builder()
                .id(UUID.randomUUID())
                .studentId(command.getStudentId())
                .recordType(command.getRecordType())
                .title(command.getTitle())
                .description(command.getDescription())
                .content(command.getContent())
                .recordDate(command.getRecordDate())
                .category(command.getCategory())
                .severity(command.getSeverity())
                .status(StudentRecordStatus.ACTIVE)
                .actionTaken(command.getActionTaken())
                .followUpRequired(command.getFollowUpRequired())
                .followUpDate(command.getFollowUpDate())
                .isConfidential(command.isConfidential())
                .schoolId(command.getSchoolId())
                .academicYearId(command.getAcademicYearId())
                .createdById(command.getCreatedById())
                .notes(command.getNotes())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return studentRecordRepository.create(record);
    }

    @Override
    public StudentRecord updateStudentRecord(UpdateStudentRecordCommand command) {
        StudentRecord record = studentRecordRepository.findById(command.getRecordId())
                .orElseThrow(() -> new RuntimeException("Student record not found"));

        record.setRecordType(command.getRecordType());
        record.setTitle(command.getTitle());
        record.setDescription(command.getDescription());
        record.setContent(command.getContent());
        record.setRecordDate(command.getRecordDate());
        record.setCategory(command.getCategory());
        record.setSeverity(command.getSeverity());
        record.setActionTaken(command.getActionTaken());
        record.setFollowUpRequired(command.getFollowUpRequired());
        record.setFollowUpDate(command.getFollowUpDate());
        record.setNotes(command.getNotes());
        record.setUpdatedAt(LocalDateTime.now());

        return studentRecordRepository.update(record);
    }

    @Override
    public StudentRecord viewStudentRecord(UUID recordId) {
        return studentRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Student record not found"));
    }

    @Override
    public PageResult<StudentRecord> viewStudentRecordsByStudent(UUID studentId, PageInfo pageInfo) {
        return studentRecordRepository.findByStudentId(studentId, pageInfo);
    }

    @Override
    public boolean deleteStudentRecord(UUID recordId) {
        return studentRecordRepository.delete(recordId);
    }
}
