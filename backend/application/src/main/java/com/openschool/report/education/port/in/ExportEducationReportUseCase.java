package com.openschool.report.education.port.in;

import com.openschool.report.education.port.in.command.ExportEducationReportCommand;
import java.util.UUID;

public interface ExportEducationReportUseCase {
    byte[] exportEducationReport(ExportEducationReportCommand command);
}

