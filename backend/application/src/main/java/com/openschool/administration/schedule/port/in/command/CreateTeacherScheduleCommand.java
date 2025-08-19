package com.openschool.administration.schedule.port.in.command;

import com.openschool.domain.schedule.ScheduleType;
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
public class CreateTeacherScheduleCommand {
    private UUID teacherId;
    private UUID schoolId;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private ScheduleType scheduleType;
    private UUID subjectId;
    private UUID classId;
    private UUID gradeId;
    private String room;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private Integer weeklyHours;
    private Integer maxWeeklyHours;
    private String notes;
}
