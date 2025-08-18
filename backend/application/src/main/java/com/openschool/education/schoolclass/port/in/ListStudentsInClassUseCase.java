package com.openschool.education.schoolclass.port.in;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.student.Student;

import java.util.UUID;

public interface ListStudentsInClassUseCase {

    /**
     * List students in class
     */
    PageResult<Student> listStudentsInClass(UUID classId, PageInfo pageInfo);
}
