package com.openschool.education.timetable.port.out;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.timetable.Timetable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TimetableRepositoryPort {
    
    Timetable create(Timetable timetable);
    
    Timetable update(Timetable timetable);
    
    Optional<Timetable> findById(UUID timetableId);
    
    PageResult<Timetable> findAll(PageInfo pageInfo);
    
    PageResult<Timetable> findByClassId(UUID classId, PageInfo pageInfo);
    
    PageResult<Timetable> findByTeacherId(UUID teacherId, PageInfo pageInfo);
    
    PageResult<Timetable> findBySubjectId(UUID subjectId, PageInfo pageInfo);
    
    List<Timetable> findConflictingTimetables(UUID classId, String dayOfWeek, 
                                              java.time.LocalTime startTime, 
                                              java.time.LocalTime endTime);
    
    boolean delete(UUID timetableId);
    
    long count();
    
    long countByClassId(UUID classId);
}
