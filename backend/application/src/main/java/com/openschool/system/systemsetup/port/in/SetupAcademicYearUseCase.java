package com.openschool.system.systemsetup.port.in;

import com.openschool.education.academic.port.in.command.CreateAcademicYearCommand;
import com.openschool.domain.systemsetup.SystemSetupStatus;

public interface SetupAcademicYearUseCase {
    SystemSetupStatus create(CreateAcademicYearCommand command);
}
