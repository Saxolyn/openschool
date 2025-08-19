package com.openschool.education.timetable.port.in;

import com.openschool.education.timetable.port.in.command.CreateTimetableCommand;
import com.openschool.domain.timetable.Timetable;

public interface CreateTimetableUseCase {
    Timetable createTimetable(CreateTimetableCommand command);
}

