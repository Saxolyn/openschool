package com.openschool.report.education.port.in.command;

import com.openschool.domain.report.ReportFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExportEducationReportCommand {
    private UUID reportId;
    private ReportFormat format;
    private UUID requestedById;
}
