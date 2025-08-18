package com.openschool.education.grade.port.in;

import java.util.UUID;

public interface DeleteGradeUseCase {

    /**
     * Delete grade
     */
    void deleteGrade(UUID gradeId);
}
