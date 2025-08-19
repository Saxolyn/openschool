package com.openschool.report.education.port.in.command;

import com.openschool.domain.report.ReportFormat;
import com.openschool.domain.report.ReportType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenerateEducationReportCommand {
    private String name;
    private String description;
    private ReportType type;
    private ReportFormat format;
    private Map<String, Object> parameters;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private String fiscalYear;
    private UUID schoolId;
    private UUID departmentId;
    private UUID generatedById;
}
