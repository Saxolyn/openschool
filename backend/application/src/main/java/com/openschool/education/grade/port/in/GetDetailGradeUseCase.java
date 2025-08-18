package com.openschool.education.grade.port.in;

import com.openschool.domain.grade.Grade;

import java.util.UUID;

public interface GetDetailGradeUseCase {

    /**
     * Get detail grade information
     */
    Grade getDetailGrade(UUID gradeId);
}
