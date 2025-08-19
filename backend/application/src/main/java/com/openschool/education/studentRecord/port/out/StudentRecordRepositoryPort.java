package com.openschool.education.studentRecord.port.out;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.studentrecord.StudentRecord;

import java.util.Optional;
import java.util.UUID;

public interface StudentRecordRepositoryPort {
    
    StudentRecord create(StudentRecord record);
    
    StudentRecord update(StudentRecord record);
    
    Optional<StudentRecord> findById(UUID recordId);
    
    PageResult<StudentRecord> findAll(PageInfo pageInfo);
    
    PageResult<StudentRecord> findByStudentId(UUID studentId, PageInfo pageInfo);
    
    PageResult<StudentRecord> findByRecordType(String recordType, PageInfo pageInfo);
    
    PageResult<StudentRecord> findByCategory(String category, PageInfo pageInfo);
    
    PageResult<StudentRecord> findBySchoolId(UUID schoolId, PageInfo pageInfo);
    
    boolean delete(UUID recordId);
    
    long count();
    
    long countByStudentId(UUID studentId);
}
