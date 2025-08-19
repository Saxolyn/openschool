package com.openschool.education.timetable.service;

import com.openschool.education.timetable.port.in.command.CreateTimetableCommand;
import com.openschool.education.timetable.port.out.TimetableRepositoryPort;
import com.openschool.domain.timetable.Timetable;
import com.openschool.domain.timetable.TimetableStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimetableServiceTest {

    @Mock
    private TimetableRepositoryPort timetableRepository;

    @InjectMocks
    private TimetableService timetableService;

    private CreateTimetableCommand createCommand;
    private Timetable timetable;

    @BeforeEach
    void setUp() {
        createCommand = CreateTimetableCommand.builder()
                .classId(UUID.randomUUID())
                .subjectId(UUID.randomUUID())
                .teacherId(UUID.randomUUID())
                .dayOfWeek("MONDAY")
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(10, 0))
                .room("A101")
                .effectiveFrom(LocalDate.now())
                .effectiveTo(LocalDate.now().plusMonths(6))
                .schoolId(UUID.randomUUID())
                .academicYearId(UUID.randomUUID())
                .semesterId(UUID.randomUUID())
                .notes("Mathematics class")
                .build();

        timetable = Timetable.builder()
                .id(UUID.randomUUID())
                .classId(createCommand.getClassId())
                .subjectId(createCommand.getSubjectId())
                .teacherId(createCommand.getTeacherId())
                .dayOfWeek(createCommand.getDayOfWeek())
                .startTime(createCommand.getStartTime())
                .endTime(createCommand.getEndTime())
                .room(createCommand.getRoom())
                .status(TimetableStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createTimetable_Success() {
        // Given
        when(timetableRepository.findConflictingTimetables(
                createCommand.getClassId(),
                createCommand.getDayOfWeek(),
                createCommand.getStartTime(),
                createCommand.getEndTime()
        )).thenReturn(Arrays.asList());
        
        when(timetableRepository.create(any(Timetable.class))).thenReturn(timetable);

        // When
        Timetable result = timetableService.createTimetable(createCommand);

        // Then
        assertNotNull(result);
        assertEquals(createCommand.getClassId(), result.getClassId());
        assertEquals(createCommand.getDayOfWeek(), result.getDayOfWeek());
        assertEquals(createCommand.getStartTime(), result.getStartTime());
        assertEquals(createCommand.getEndTime(), result.getEndTime());
        assertEquals(TimetableStatus.ACTIVE, result.getStatus());
        verify(timetableRepository).create(any(Timetable.class));
    }

    @Test
    void createTimetable_ConflictDetected_ThrowsException() {
        // Given
        List<Timetable> conflicts = Arrays.asList(timetable);
        when(timetableRepository.findConflictingTimetables(
                createCommand.getClassId(),
                createCommand.getDayOfWeek(),
                createCommand.getStartTime(),
                createCommand.getEndTime()
        )).thenReturn(conflicts);

        // When & Then
        assertThrows(RuntimeException.class, () -> timetableService.createTimetable(createCommand));
        verify(timetableRepository, never()).create(any(Timetable.class));
    }

    @Test
    void viewTimetable_Success() {
        // Given
        when(timetableRepository.findById(timetable.getId())).thenReturn(Optional.of(timetable));

        // When
        Timetable result = timetableService.viewTimetable(timetable.getId());

        // Then
        assertNotNull(result);
        assertEquals(timetable.getId(), result.getId());
        verify(timetableRepository).findById(timetable.getId());
    }

    @Test
    void viewTimetable_NotFound_ThrowsException() {
        // Given
        when(timetableRepository.findById(timetable.getId())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> timetableService.viewTimetable(timetable.getId()));
    }
}
