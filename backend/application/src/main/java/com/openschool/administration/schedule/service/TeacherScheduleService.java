package com.openschool.administration.schedule.service;

import com.openschool.administration.schedule.port.in.*;
import com.openschool.administration.schedule.port.in.command.*;
import com.openschool.administration.schedule.port.out.*;
import com.openschool.domain.schedule.*;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class TeacherScheduleService implements CreateTeacherScheduleUseCase {

    private final TeacherScheduleRepositoryPort teacherScheduleRepository;

    @Override
    public TeacherSchedule createTeacherSchedule(CreateTeacherScheduleCommand command) {
        // Check for conflicts
        List<TeacherSchedule> conflicts = teacherScheduleRepository.findConflictingSchedules(
                command.getTeacherId(), 
                command.getDayOfWeek(), 
                command.getStartTime(), 
                command.getEndTime()
        );

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Schedule conflict detected for teacher");
        }

        TeacherSchedule schedule = TeacherSchedule.builder()
                .id(UUID.randomUUID())
                .teacherId(command.getTeacherId())
                .schoolId(command.getSchoolId())
                .dayOfWeek(command.getDayOfWeek())
                .startTime(command.getStartTime())
                .endTime(command.getEndTime())
                .scheduleType(command.getScheduleType())
                .subjectId(command.getSubjectId())
                .classId(command.getClassId())
                .gradeId(command.getGradeId())
                .room(command.getRoom())
                .status(ScheduleStatus.ACTIVE)
                .isAvailable(true)
                .effectiveFrom(command.getEffectiveFrom())
                .effectiveTo(command.getEffectiveTo())
                .weeklyHours(command.getWeeklyHours())
                .maxWeeklyHours(command.getMaxWeeklyHours())
                .notes(command.getNotes())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return teacherScheduleRepository.create(schedule);
    }
}
