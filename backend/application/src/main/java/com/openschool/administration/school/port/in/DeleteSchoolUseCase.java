package com.openschool.administration.school.port.in;

import java.util.UUID;

public interface DeleteSchoolUseCase {

    /**
     * Delete school
     */
    void deleteSchool(UUID schoolId);
}
