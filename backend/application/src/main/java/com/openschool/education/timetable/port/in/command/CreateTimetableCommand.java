package com.openschool.education.timetable.port.in.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTimetableCommand {
    private UUID classId;
    private UUID subjectId;
    private UUID teacherId;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private String room;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private UUID schoolId;
    private UUID academicYearId;
    private UUID semesterId;
    private String notes;
}
