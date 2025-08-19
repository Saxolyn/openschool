package com.openschool.administration.schedule.port.out;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.schedule.TeacherSchedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeacherScheduleRepositoryPort {
    
    TeacherSchedule create(TeacherSchedule schedule);
    
    TeacherSchedule update(TeacherSchedule schedule);
    
    Optional<TeacherSchedule> findById(UUID scheduleId);
    
    PageResult<TeacherSchedule> findAll(PageInfo pageInfo);
    
    PageResult<TeacherSchedule> findByTeacherId(UUID teacherId, PageInfo pageInfo);
    
    PageResult<TeacherSchedule> findBySchoolId(UUID schoolId, PageInfo pageInfo);
    
    List<TeacherSchedule> findByTeacherIdAndDate(UUID teacherId, LocalDate date);
    
    List<TeacherSchedule> findConflictingSchedules(UUID teacherId, String dayOfWeek, 
                                                   java.time.LocalTime startTime, 
                                                   java.time.LocalTime endTime);
    
    boolean delete(UUID scheduleId);
    
    long count();
    
    long countByTeacherId(UUID teacherId);
}
