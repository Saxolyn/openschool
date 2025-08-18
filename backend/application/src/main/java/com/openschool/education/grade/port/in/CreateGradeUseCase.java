package com.openschool.education.grade.port.in;

import com.openschool.domain.grade.Grade;
import com.openschool.education.grade.port.in.command.CreateGradeCommand;

import java.util.UUID;

public interface CreateGradeUseCase {
    Grade createGrade(CreateGradeCommand command, UUID schoolId);
}
