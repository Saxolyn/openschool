package com.openschool.education.grade.port.in;

import com.openschool.domain.grade.Grade;
import com.openschool.education.grade.port.in.command.UpdateGradeCommand;

import java.util.UUID;

public interface UpdateGradeUseCase {

    /**
     * Update grade information
     */
    Grade updateGrade(UpdateGradeCommand command, UUID gradeId);
}
