package com.openschool.report.education.port.in;

import com.openschool.report.education.port.in.command.GenerateEducationReportCommand;
import com.openschool.domain.report.Report;

public interface GenerateEducationReportUseCase {
    Report generateEducationReport(GenerateEducationReportCommand command);
}

