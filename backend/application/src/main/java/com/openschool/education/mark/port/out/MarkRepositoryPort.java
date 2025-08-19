package com.openschool.education.mark.port.out;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.mark.Mark;

import java.util.Optional;
import java.util.UUID;

public interface MarkRepositoryPort {
    
    Mark create(Mark mark);
    
    Mark update(Mark mark);
    
    Optional<Mark> findById(UUID markId);
    
    PageResult<Mark> findAll(PageInfo pageInfo);
    
    PageResult<Mark> findByStudentId(UUID studentId, PageInfo pageInfo);
    
    PageResult<Mark> findBySubjectId(UUID subjectId, PageInfo pageInfo);
    
    PageResult<Mark> findByExamId(UUID examId, PageInfo pageInfo);
    
    PageResult<Mark> findByClassId(UUID classId, PageInfo pageInfo);
    
    boolean delete(UUID markId);
    
    long count();
    
    Double calculateAverageByStudentId(UUID studentId);
}
