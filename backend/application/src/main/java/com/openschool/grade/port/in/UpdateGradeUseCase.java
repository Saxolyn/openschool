package com.openschool.grade.port.in;

import com.openschool.domain.grade.Grade;
import com.openschool.grade.port.in.command.CreateGradeCommand;
import com.openschool.grade.port.in.command.UpdateGradeCommand;

import java.util.UUID;

public interface UpdateGradeUseCase {
    Grade update(UpdateGradeCommand command, UUID schoolId);
}
