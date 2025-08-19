package com.openschool.education.exam.service;

import com.openschool.education.exam.port.in.command.CreateExamCommand;
import com.openschool.education.exam.port.out.ExamRepositoryPort;
import com.openschool.domain.exam.Exam;
import com.openschool.domain.exam.ExamStatus;
import com.openschool.domain.exam.ExamType;
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
class ExamServiceTest {

    @Mock
    private ExamRepositoryPort examRepository;

    @InjectMocks
    private ExamService examService;

    private CreateExamCommand createCommand;
    private Exam exam;

    @BeforeEach
    void setUp() {
        createCommand = CreateExamCommand.builder()
                .title("Midterm Math Exam")
                .description("Mathematics midterm examination")
                .subject("Mathematics")
                .type(ExamType.MIDTERM)
                .startTime(LocalDateTime.now().plusDays(7))
                .endTime(LocalDateTime.now().plusDays(7).plusHours(2))
                .durationMinutes(120)
                .schoolId(UUID.randomUUID())
                .academicYearId(UUID.randomUUID())
                .gradeId(UUID.randomUUID())
                .classId(UUID.randomUUID())
                .totalMarks(100)
                .passingMarks(50)
                .instructions("Read all questions carefully")
                .maxStudents(30)
                .supervisorId(UUID.randomUUID())
                .venue("Main Hall")
                .roomNumber("A101")
                .build();

        exam = Exam.builder()
                .id(UUID.randomUUID())
                .title(createCommand.getTitle())
                .description(createCommand.getDescription())
                .subject(createCommand.getSubject())
                .type(createCommand.getType())
                .status(ExamStatus.DRAFT)
                .totalMarks(createCommand.getTotalMarks())
                .passingMarks(createCommand.getPassingMarks())
                .registeredStudents(0)
                .resultsPublished(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createExam_Success() {
        // Given
        when(examRepository.create(any(Exam.class))).thenReturn(exam);

        // When
        Exam result = examService.createExam(createCommand);

        // Then
        assertNotNull(result);
        assertEquals("Midterm Math Exam", result.getTitle());
        assertEquals("Mathematics", result.getSubject());
        assertEquals(ExamType.MIDTERM, result.getType());
        assertEquals(ExamStatus.DRAFT, result.getStatus());
        assertEquals(100, result.getTotalMarks());
        assertEquals(50, result.getPassingMarks());
        assertEquals(0, result.getRegisteredStudents());
        assertFalse(result.isResultsPublished());
        
        verify(examRepository).create(any(Exam.class));
    }

    @Test
    void createExam_VerifyDefaultValues() {
        // Given
        when(examRepository.create(any(Exam.class))).thenReturn(exam);

        // When
        Exam result = examService.createExam(createCommand);

        // Then
        assertEquals(ExamStatus.DRAFT, result.getStatus());
        assertEquals(0, result.getRegisteredStudents());
        assertFalse(result.isResultsPublished());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
    }
}
