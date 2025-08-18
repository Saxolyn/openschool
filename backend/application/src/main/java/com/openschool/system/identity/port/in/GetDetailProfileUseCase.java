package com.openschool.system.identity.port.in;

import com.openschool.domain.grade.Grade;
import com.openschool.domain.identity.model.Profile;

import java.util.UUID;

public interface GetDetailProfileUseCase {
    Profile getListGrade(UUID profileId);
}
