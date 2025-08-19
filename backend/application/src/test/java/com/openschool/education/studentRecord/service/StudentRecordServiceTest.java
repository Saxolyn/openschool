package com.openschool.education.studentRecord.service;

import com.openschool.education.studentRecord.port.in.command.CreateStudentRecordCommand;
import com.openschool.education.studentRecord.port.in.command.UpdateStudentRecordCommand;
import com.openschool.education.studentRecord.port.out.StudentRecordRepositoryPort;
import com.openschool.domain.studentrecord.StudentRecord;
import com.openschool.domain.studentrecord.StudentRecordStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentRecordServiceTest {

    @Mock
    private StudentRecordRepositoryPort studentRecordRepository;

    @InjectMocks
    private StudentRecordService studentRecordService;

    private CreateStudentRecordCommand createCommand;
    private UpdateStudentRecordCommand updateCommand;
    private StudentRecord studentRecord;

    @BeforeEach
    void setUp() {
        createCommand = CreateStudentRecordCommand.builder()
                .studentId(UUID.randomUUID())
                .recordType("DISCIPLINARY")
                .title("Late to Class")
                .description("Student was late to mathematics class")
                .content("Student arrived 15 minutes late without excuse")
                .recordDate(LocalDate.now())
                .category("ATTENDANCE")
                .severity("MINOR")
                .actionTaken("Verbal warning given")
                .followUpRequired("Monitor attendance")
                .followUpDate(LocalDate.now().plusWeeks(1))
                .confidential(false)
                .schoolId(UUID.randomUUID())
                .academicYearId(UUID.randomUUID())
                .createdById(UUID.randomUUID())
                .notes("First offense")
                .build();

        studentRecord = StudentRecord.builder()
                .id(UUID.randomUUID())
                .studentId(createCommand.getStudentId())
                .recordType(createCommand.getRecordType())
                .title(createCommand.getTitle())
                .status(StudentRecordStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        updateCommand = UpdateStudentRecordCommand.builder()
                .recordId(studentRecord.getId())
                .recordType("DISCIPLINARY")
                .title("Updated Title")
                .description("Updated description")
                .severity("MODERATE")
                .actionTaken("Written warning")
                .notes("Updated notes")
                .build();
    }

    @Test
    void createStudentRecord_Success() {
        // Given
        when(studentRecordRepository.create(any(StudentRecord.class))).thenReturn(studentRecord);

        // When
        StudentRecord result = studentRecordService.createStudentRecord(createCommand);

        // Then
        assertNotNull(result);
        assertEquals(createCommand.getStudentId(), result.getStudentId());
        assertEquals(createCommand.getRecordType(), result.getRecordType());
        assertEquals(createCommand.getTitle(), result.getTitle());
        assertEquals(StudentRecordStatus.ACTIVE, result.getStatus());
        verify(studentRecordRepository).create(any(StudentRecord.class));
    }

    @Test
    void updateStudentRecord_Success() {
        // Given
        when(studentRecordRepository.findById(studentRecord.getId())).thenReturn(Optional.of(studentRecord));
        when(studentRecordRepository.update(any(StudentRecord.class))).thenReturn(studentRecord);

        // When
        StudentRecord result = studentRecordService.updateStudentRecord(updateCommand);

        // Then
        assertNotNull(result);
        verify(studentRecordRepository).findById(studentRecord.getId());
        verify(studentRecordRepository).update(any(StudentRecord.class));
    }

    @Test
    void updateStudentRecord_NotFound_ThrowsException() {
        // Given
        when(studentRecordRepository.findById(studentRecord.getId())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> studentRecordService.updateStudentRecord(updateCommand));
    }

    @Test
    void viewStudentRecord_Success() {
        // Given
        when(studentRecordRepository.findById(studentRecord.getId())).thenReturn(Optional.of(studentRecord));

        // When
        StudentRecord result = studentRecordService.viewStudentRecord(studentRecord.getId());

        // Then
        assertNotNull(result);
        assertEquals(studentRecord.getId(), result.getId());
        verify(studentRecordRepository).findById(studentRecord.getId());
    }

    @Test
    void deleteStudentRecord_Success() {
        // Given
        when(studentRecordRepository.delete(studentRecord.getId())).thenReturn(true);

        // When
        boolean result = studentRecordService.deleteStudentRecord(studentRecord.getId());

        // Then
        assertTrue(result);
        verify(studentRecordRepository).delete(studentRecord.getId());
    }
}
