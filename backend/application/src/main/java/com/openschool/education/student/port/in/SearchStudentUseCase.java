package com.openschool.education.student.port.in;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.student.Student;

public interface SearchStudentUseCase {

    /**
     * Search students by criteria
     */
    PageResult<Student> searchStudent(String searchTerm, PageInfo pageInfo);
}
