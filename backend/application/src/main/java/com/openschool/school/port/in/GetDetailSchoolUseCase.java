package com.openschool.school.port.in;

import com.openschool.domain.grade.Grade;
import com.openschool.domain.school.School;

import java.util.UUID;

public interface GetDetailSchoolUseCase {
    School getSchool();
}
