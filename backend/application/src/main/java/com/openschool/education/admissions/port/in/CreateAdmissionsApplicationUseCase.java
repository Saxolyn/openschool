package com.openschool.education.admissions.port.in;

import com.openschool.education.admissions.port.in.command.CreateAdmissionsApplicationCommand;
import com.openschool.domain.admissions.AdmissionsApplication;

public interface CreateAdmissionsApplicationUseCase {
    AdmissionsApplication createAdmissionsApplication(CreateAdmissionsApplicationCommand command);
}

