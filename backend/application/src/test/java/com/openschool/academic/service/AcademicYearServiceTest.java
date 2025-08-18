package com.openschool.academic.service;

import com.openschool.education.academic.port.in.command.CreateAcademicYearCommand;
import com.openschool.education.academic.port.in.command.CreateSemesterCommand;
import com.openschool.education.academic.port.in.command.UpdateAcademicYearCommand;
import com.openschool.education.academic.port.out.AcademicYearRepositoryPort;
import com.openschool.common.exception.DataNotFound;
import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.academic.AcademicYear;
import com.openschool.domain.academic.AcademicYearStatus;
import com.openschool.domain.school.School;
import com.openschool.education.academic.service.AcademicYearService;
import com.openschool.administration.school.port.out.SchoolRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AcademicYearServiceTest {

    @Mock
    private AcademicYearRepositoryPort academicYearRepository;

    @Mock
    private SchoolRepositoryPort schoolRepository;

    @InjectMocks
    private AcademicYearService academicYearService;

    private UUID schoolId;
    private UUID academicYearId;
    private School school;
    private AcademicYear academicYear;

    @BeforeEach
    void setUp() {
        schoolId = UUID.randomUUID();
        academicYearId = UUID.randomUUID();

        school = School.builder()
                .id(schoolId)
                .name("Test School")
                .build();

        academicYear = AcademicYear.builder()
                .id(academicYearId)
                .schoolId(schoolId)
                .code("2024-2025")
                .name("Academic Year 2024-2025")
                .startDate(LocalDate.of(2024, 9, 1))
                .endDate(LocalDate.of(2025, 6, 30))
                .status(AcademicYearStatus.ACTIVE)
                .semesters(Collections.emptyList())
                .build();
    }

    @Test
    void create_ShouldReturnAcademicYearWithGeneratedIds() {
        // Arrange
        CreateSemesterCommand semester1 = new CreateSemesterCommand();
        semester1.setName("Semester 1");
        semester1.setStartDate(LocalDate.of(2025, 1, 1));
        semester1.setEndDate(LocalDate.of(2025, 6, 1));
        CreateSemesterCommand semester2 = new CreateSemesterCommand();
        semester2.setName("Semester 2");
        semester2.setStartDate(LocalDate.of(2025, 7, 1));
        semester2.setEndDate(LocalDate.of(2025, 12, 1));
        List<CreateSemesterCommand> semesters = Arrays.asList(semester1, semester2);

        UUID schoolId = UUID.randomUUID();
        School mockSchool = mock(School.class);
        when(schoolRepository.findById(schoolId)).thenReturn(Optional.of(mockSchool));
        CreateAcademicYearCommand command = CreateAcademicYearCommand.builder()
                .schoolId(schoolId)
                .code("2025-2026")
                .name("Academic Year 2025-2026")
                .startDate(LocalDate.of(2025, 1, 1))
                .endDate(LocalDate.of(2025, 12, 31))
                .status(AcademicYearStatus.ACTIVE)
                .semesters(semesters)
                .build();

        when(academicYearRepository.create(any(AcademicYear.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        AcademicYear result = academicYearService.create(command);

        // Assert
        assertNotNull(result.getId());
        assertEquals(command.getCode(), result.getCode());
        assertEquals(command.getName(), result.getName());
        assertEquals(command.getStartDate(), result.getStartDate());
        assertEquals(command.getEndDate(), result.getEndDate());
        assertEquals(command.getStatus(), result.getStatus());
        assertEquals(2, result.getSemesters().size());
        result.getSemesters().forEach(s -> assertNotNull(s.getId()));
        verify(academicYearRepository, times(1)).create(any(AcademicYear.class));
    }

    @Test
    void create_ShouldThrowDataNotFound_WhenSchoolNotExists() {
        // Given
        CreateAcademicYearCommand command = CreateAcademicYearCommand.builder()
                .schoolId(schoolId)
                .code("2024-2025")
                .name("Academic Year 2024-2025")
                .startDate(LocalDate.of(2024, 9, 1))
                .endDate(LocalDate.of(2025, 6, 30))
                .status(AcademicYearStatus.ACTIVE)
                .semesters(Collections.emptyList())
                .build();
        when(schoolRepository.findById(schoolId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(DataNotFound.class, () -> academicYearService.create(command));
        verify(schoolRepository).findById(schoolId);
        verify(academicYearRepository, never()).create(any(AcademicYear.class));
    }

    @Test
    void update_ShouldUpdateAcademicYear_WhenValidCommand() {
        // Given
        UpdateAcademicYearCommand updateCommand = UpdateAcademicYearCommand.builder()
                .name("Updated Academic Year 2024-2025")
                .build();
        when(academicYearRepository.findById(academicYearId)).thenReturn(Optional.of(academicYear));
        when(academicYearRepository.update(any(AcademicYear.class))).thenReturn(academicYear);

        // When
        AcademicYear result = academicYearService.update(updateCommand, academicYearId);

        // Then
        assertNotNull(result);
        verify(academicYearRepository).findById(academicYearId);
        verify(academicYearRepository).update(any(AcademicYear.class));
    }

    @Test
    void update_ShouldThrowDataNotFound_WhenAcademicYearNotExists() {
        // Given
        UpdateAcademicYearCommand updateCommand = UpdateAcademicYearCommand.builder()
                .name("Updated Academic Year 2024-2025")
                .build();
        when(academicYearRepository.findById(academicYearId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(DataNotFound.class, () -> academicYearService.update(updateCommand, academicYearId));
        verify(academicYearRepository).findById(academicYearId);
        verify(academicYearRepository, never()).update(any(AcademicYear.class));
    }

    @Test
    void delete_ShouldDeleteAcademicYear_WhenExists() {
        // Given
        when(academicYearRepository.findById(academicYearId)).thenReturn(Optional.of(academicYear));

        // When
        academicYearService.delete(academicYearId);

        // Then
        verify(academicYearRepository).findById(academicYearId);
        verify(academicYearRepository).delete(academicYearId);
    }

    @Test
    void delete_ShouldThrowDataNotFound_WhenAcademicYearNotExists() {
        // Given
        when(academicYearRepository.findById(academicYearId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(DataNotFound.class, () -> academicYearService.delete(academicYearId));
        verify(academicYearRepository).findById(academicYearId);
        verify(academicYearRepository, never()).delete(academicYearId);
    }

    @Test
    void getDetail_ShouldReturnAcademicYear_WhenExists() {
        // Given
        when(academicYearRepository.findById(academicYearId)).thenReturn(Optional.of(academicYear));

        // When
        AcademicYear result = academicYearService.getDetail(academicYearId);

        // Then
        assertNotNull(result);
        assertEquals(academicYear.getId(), result.getId());
        assertEquals(academicYear.getName(), result.getName());
        verify(academicYearRepository).findById(academicYearId);
    }

    @Test
    void getDetail_ShouldThrowDataNotFound_WhenAcademicYearNotExists() {
        // Given
        when(academicYearRepository.findById(academicYearId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(DataNotFound.class, () -> academicYearService.getDetail(academicYearId));
        verify(academicYearRepository).findById(academicYearId);
    }

    @Test
    void getList_ShouldReturnPageResult() {
        // Given
        PageInfo pageInfo = new PageInfo(0, 10);
        PageResult<AcademicYear> expectedResult = new PageResult<>(Collections.singletonList(academicYear), 1, 0, 10);
        when(academicYearRepository.findAll(pageInfo)).thenReturn(expectedResult);

        // When
        PageResult<AcademicYear> result = academicYearService.getList(pageInfo);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        verify(academicYearRepository).findAll(pageInfo);
    }
}
