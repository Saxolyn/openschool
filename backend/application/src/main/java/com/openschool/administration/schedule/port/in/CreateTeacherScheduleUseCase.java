package com.openschool.administration.schedule.port.in;

import com.openschool.administration.schedule.port.in.command.CreateTeacherScheduleCommand;
import com.openschool.domain.schedule.TeacherSchedule;

public interface CreateTeacherScheduleUseCase {
    TeacherSchedule createTeacherSchedule(CreateTeacherScheduleCommand command);
}
