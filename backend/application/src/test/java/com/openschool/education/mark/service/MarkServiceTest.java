package com.openschool.education.mark.service;

import com.openschool.education.mark.port.in.command.CreateMarkCommand;
import com.openschool.education.mark.port.out.MarkRepositoryPort;
import com.openschool.domain.mark.Mark;
import com.openschool.domain.mark.MarkStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MarkServiceTest {

    @Mock
    private MarkRepositoryPort markRepository;

    @InjectMocks
    private MarkService markService;

    private CreateMarkCommand createCommand;
    private Mark mark;

    @BeforeEach
    void setUp() {
        createCommand = CreateMarkCommand.builder()
                .studentId(UUID.randomUUID())
                .subjectId(UUID.randomUUID())
                .examId(UUID.randomUUID())
                .classId(UUID.randomUUID())
                .assessmentType("EXAM")
                .assessmentName("Midterm Exam")
                .obtainedMarks(85.0)
                .totalMarks(100.0)
                .academicYearId(UUID.randomUUID())
                .semesterId(UUID.randomUUID())
                .term("FIRST")
                .enteredById(UUID.randomUUID())
                .teacherComments("Good performance")
                .feedback("Keep up the good work")
                .build();

        mark = Mark.builder()
                .id(UUID.randomUUID())
                .studentId(createCommand.getStudentId())
                .obtainedMarks(createCommand.getObtainedMarks())
                .totalMarks(createCommand.getTotalMarks())
                .status(MarkStatus.DRAFT)
                .appealSubmitted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createMark_Success() {
        // Given
        when(markRepository.create(any(Mark.class))).thenReturn(mark);

        // When
        Mark result = markService.createMark(createCommand);

        // Then
        assertNotNull(result);
        assertEquals(createCommand.getStudentId(), result.getStudentId());
        assertEquals(createCommand.getObtainedMarks(), result.getObtainedMarks());
        assertEquals(createCommand.getTotalMarks(), result.getTotalMarks());
        assertEquals(MarkStatus.DRAFT, result.getStatus());
        assertFalse(result.isAppealSubmitted());
        assertEquals("Good performance", result.getTeacherComments());
        verify(markRepository).create(any(Mark.class));
    }

    @Test
    void createMark_VerifyDefaultValues() {
        // Given
        when(markRepository.create(any(Mark.class))).thenReturn(mark);

        // When
        Mark result = markService.createMark(createCommand);

        // Then
        assertEquals(MarkStatus.DRAFT, result.getStatus());
        assertFalse(result.isAppealSubmitted());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
    }
}
