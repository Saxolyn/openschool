package com.openschool.system.systemsetup.port.in;

import com.openschool.domain.systemsetup.SystemSetupStatus;
import com.openschool.administration.school.port.in.command.CreateSchoolCommand;

public interface SetupSchoolProfileUseCase {
    SystemSetupStatus createSchoolProfile(CreateSchoolCommand command);
}
