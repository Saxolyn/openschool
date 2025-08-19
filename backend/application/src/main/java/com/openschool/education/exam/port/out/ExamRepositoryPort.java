package com.openschool.education.exam.port.out;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.exam.Exam;

import java.util.Optional;
import java.util.UUID;

public interface ExamRepositoryPort {
    
    Exam create(Exam exam);
    
    Exam update(Exam exam);
    
    Optional<Exam> findById(UUID examId);
    
    PageResult<Exam> findAll(PageInfo pageInfo);
    
    PageResult<Exam> findBySchoolId(UUID schoolId, PageInfo pageInfo);
    
    PageResult<Exam> findByGradeId(UUID gradeId, PageInfo pageInfo);
    
    PageResult<Exam> findByClassId(UUID classId, PageInfo pageInfo);
    
    PageResult<Exam> findBySubject(String subject, PageInfo pageInfo);
    
    boolean delete(UUID examId);
    
    long count();
}
