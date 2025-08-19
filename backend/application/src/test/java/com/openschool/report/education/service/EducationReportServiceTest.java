package com.openschool.report.education.service;

import com.openschool.report.education.port.in.command.ExportEducationReportCommand;
import com.openschool.report.education.port.in.command.GenerateEducationReportCommand;
import com.openschool.report.education.port.out.ReportRepositoryPort;
import com.openschool.domain.report.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EducationReportServiceTest {

    @Mock
    private ReportRepositoryPort reportRepository;

    @InjectMocks
    private EducationReportService educationReportService;

    private GenerateEducationReportCommand generateCommand;
    private ExportEducationReportCommand exportCommand;
    private Report report;

    @BeforeEach
    void setUp() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("includeGrades", true);
        parameters.put("includeAttendance", false);

        generateCommand = GenerateEducationReportCommand.builder()
                .name("Student Performance Report")
                .description("Comprehensive student performance analysis")
                .type(ReportType.EDUCATION)
                .format(ReportFormat.PDF)
                .parameters(parameters)
                .periodStart(LocalDateTime.now().minusMonths(3))
                .periodEnd(LocalDateTime.now())
                .fiscalYear("2024")
                .schoolId(UUID.randomUUID())
                .departmentId(UUID.randomUUID())
                .generatedById(UUID.randomUUID())
                .build();

        report = Report.builder()
                .id(UUID.randomUUID())
                .name(generateCommand.getName())
                .description(generateCommand.getDescription())
                .type(generateCommand.getType())
                .category(ReportCategory.ACADEMIC)
                .status(ReportStatus.COMPLETED)
                .fileName("student_performance_report_123456.pdf")
                .filePath("/reports/student_performance_report_123456.pdf")
                .fileSize(2048L)
                .isPublic(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        exportCommand = ExportEducationReportCommand.builder()
                .reportId(report.getId())
                .format(ReportFormat.PDF)
                .requestedById(UUID.randomUUID())
                .build();
    }

    @Test
    void generateEducationReport_Success() {
        // Given
        when(reportRepository.create(any(Report.class))).thenReturn(report);
        when(reportRepository.update(any(Report.class))).thenReturn(report);

        // When
        Report result = educationReportService.generateEducationReport(generateCommand);

        // Then
        assertNotNull(result);
        assertEquals("Student Performance Report", result.getName());
        assertEquals(ReportType.EDUCATION, result.getType());
        assertEquals(ReportCategory.ACADEMIC, result.getCategory());
        assertFalse(result.isPublic());
        verify(reportRepository).create(any(Report.class));
        verify(reportRepository).update(any(Report.class));
    }

    @Test
    void exportEducationReport_Success() {
        // Given
        when(reportRepository.findById(report.getId())).thenReturn(Optional.of(report));

        // When
        byte[] result = educationReportService.exportEducationReport(exportCommand);

        // Then
        assertNotNull(result);
        verify(reportRepository).findById(report.getId());
    }

    @Test
    void exportEducationReport_ReportNotFound_ThrowsException() {
        // Given
        when(reportRepository.findById(report.getId())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> educationReportService.exportEducationReport(exportCommand));
    }

    @Test
    void exportEducationReport_ReportNotReady_ThrowsException() {
        // Given
        report.setStatus(ReportStatus.GENERATING);
        when(reportRepository.findById(report.getId())).thenReturn(Optional.of(report));

        // When & Then
        assertThrows(RuntimeException.class, () -> educationReportService.exportEducationReport(exportCommand));
    }
}
