package com.openschool.education.exam.service;

import com.openschool.education.exam.port.in.CreateExamUseCase;
import com.openschool.education.exam.port.in.command.CreateExamCommand;
import com.openschool.education.exam.port.out.ExamRepositoryPort;
import com.openschool.domain.exam.Exam;
import com.openschool.domain.exam.ExamStatus;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public class ExamService implements CreateExamUseCase {

    private final ExamRepositoryPort examRepository;

    @Override
    public Exam createExam(CreateExamCommand command) {
        Exam exam = Exam.builder()
                .id(UUID.randomUUID())
                .title(command.getTitle())
                .description(command.getDescription())
                .subject(command.getSubject())
                .type(command.getType())
                .startTime(command.getStartTime())
                .endTime(command.getEndTime())
                .durationMinutes(command.getDurationMinutes())
                .schoolId(command.getSchoolId())
                .academicYearId(command.getAcademicYearId())
                .gradeId(command.getGradeId())
                .classId(command.getClassId())
                .totalMarks(command.getTotalMarks())
                .passingMarks(command.getPassingMarks())
                .status(ExamStatus.DRAFT)
                .instructions(command.getInstructions())
                .maxStudents(command.getMaxStudents())
                .registeredStudents(0)
                .supervisorId(command.getSupervisorId())
                .venue(command.getVenue())
                .roomNumber(command.getRoomNumber())
                .resultsPublished(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return examRepository.create(exam);
    }
}
