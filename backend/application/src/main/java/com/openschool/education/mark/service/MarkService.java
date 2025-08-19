package com.openschool.education.mark.service;

import com.openschool.education.mark.port.in.*;
import com.openschool.education.mark.port.in.command.*;
import com.openschool.education.mark.port.out.*;
import com.openschool.domain.mark.*;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public class MarkService implements CreateMarkUseCase {

    private final MarkRepositoryPort markRepository;

    @Override
    public Mark createMark(CreateMarkCommand command) {
        Mark mark = Mark.builder()
                .id(UUID.randomUUID())
                .studentId(command.getStudentId())
                .subjectId(command.getSubjectId())
                .examId(command.getExamId())
                .classId(command.getClassId())
                .assessmentType(command.getAssessmentType())
                .assessmentName(command.getAssessmentName())
                .obtainedMarks(command.getObtainedMarks())
                .totalMarks(command.getTotalMarks())
                .academicYearId(command.getAcademicYearId())
                .semesterId(command.getSemesterId())
                .term(command.getTerm())
                .status(MarkStatus.DRAFT)
                .enteredById(command.getEnteredById())
                .teacherComments(command.getTeacherComments())
                .feedback(command.getFeedback())
                .appealSubmitted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Calculate grade automatically
        mark.calculateGrade();

        return markRepository.create(mark);
    }
}
