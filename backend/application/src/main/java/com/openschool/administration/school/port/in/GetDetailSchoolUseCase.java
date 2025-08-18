package com.openschool.administration.school.port.in;

import com.openschool.domain.school.School;

import java.util.UUID;

public interface GetDetailSchoolUseCase {

    /**
     * Get detail school information
     */
    School getDetailSchool(UUID schoolId);
}
