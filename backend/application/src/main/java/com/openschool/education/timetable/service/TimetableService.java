package com.openschool.education.timetable.service;

import com.openschool.education.timetable.port.in.*;
import com.openschool.education.timetable.port.in.command.*;
import com.openschool.education.timetable.port.out.*;
import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.timetable.*;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class TimetableService implements
        CreateTimetableUseCase,
        ViewTimetableUseCase {

    private final TimetableRepositoryPort timetableRepository;

    @Override
    public Timetable createTimetable(CreateTimetableCommand command) {
        // Check for conflicts
        List<Timetable> conflicts = timetableRepository.findConflictingTimetables(
                command.getClassId(),
                command.getDayOfWeek(),
                command.getStartTime(),
                command.getEndTime()
        );

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Timetable conflict detected for class");
        }

        Timetable timetable = Timetable.builder()
                .id(UUID.randomUUID())
                .classId(command.getClassId())
                .subjectId(command.getSubjectId())
                .teacherId(command.getTeacherId())
                .dayOfWeek(command.getDayOfWeek())
                .startTime(command.getStartTime())
                .endTime(command.getEndTime())
                .room(command.getRoom())
                .status(TimetableStatus.ACTIVE)
                .effectiveFrom(command.getEffectiveFrom())
                .effectiveTo(command.getEffectiveTo())
                .schoolId(command.getSchoolId())
                .academicYearId(command.getAcademicYearId())
                .semesterId(command.getSemesterId())
                .notes(command.getNotes())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return timetableRepository.create(timetable);
    }

    @Override
    public Timetable viewTimetable(UUID timetableId) {
        return timetableRepository.findById(timetableId)
                .orElseThrow(() -> new RuntimeException("Timetable not found"));
    }

    @Override
    public PageResult<Timetable> viewTimetablesByClass(UUID classId, PageInfo pageInfo) {
        return timetableRepository.findByClassId(classId, pageInfo);
    }
}
