package com.openschool.report.education.service;

import com.openschool.report.education.port.in.*;
import com.openschool.report.education.port.in.command.*;
import com.openschool.report.education.port.out.*;
import com.openschool.domain.report.*;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public class EducationReportService implements
        GenerateEducationReportUseCase,
        ExportEducationReportUseCase {

    private final ReportRepositoryPort reportRepository;

    @Override
    public Report generateEducationReport(GenerateEducationReportCommand command) {
        Report report = Report.builder()
                .id(UUID.randomUUID())
                .name(command.getName())
                .description(command.getDescription())
                .type(command.getType())
                .category(ReportCategory.ACADEMIC)
                .parameters(command.getParameters())
                .format(command.getFormat())
                .periodStart(command.getPeriodStart())
                .periodEnd(command.getPeriodEnd())
                .fiscalYear(command.getFiscalYear())
                .status(ReportStatus.PENDING)
                .schoolId(command.getSchoolId())
                .departmentId(command.getDepartmentId())
                .isPublic(false)
                .accessLevel("DEPARTMENT")
                .isScheduled(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Start generation process
        report.startGeneration(command.getGeneratedById());

        Report savedReport = reportRepository.create(report);

        // Simulate report generation
        try {
            // In real implementation, this would generate the actual report
            Thread.sleep(100); // Simulate processing time
            
            String fileName = generateFileName(savedReport);
            String filePath = "/reports/" + fileName;
            Long fileSize = 1024L; // Simulated file size
            
            savedReport.completeGeneration(fileName, filePath, fileSize);
            
        } catch (Exception e) {
            savedReport.failGeneration("Report generation failed: " + e.getMessage());
        }

        return reportRepository.update(savedReport);
    }

    @Override
    public byte[] exportEducationReport(ExportEducationReportCommand command) {
        Report report = reportRepository.findById(command.getReportId())
                .orElseThrow(() -> new RuntimeException("Report not found"));

        if (!report.isGenerated()) {
            throw new RuntimeException("Report is not ready for export");
        }

        if (!report.isAccessibleBy(command.getRequestedById(), "USER")) {
            throw new RuntimeException("Access denied to this report");
        }

        // In real implementation, this would read the actual file
        // For now, return empty byte array
        return new byte[0];
    }

    private String generateFileName(Report report) {
        return String.format("%s_%s_%d.%s",
                report.getName().replaceAll("\\s+", "_"),
                report.getType().toString().toLowerCase(),
                System.currentTimeMillis(),
                report.getFormat().toString().toLowerCase());
    }
}
