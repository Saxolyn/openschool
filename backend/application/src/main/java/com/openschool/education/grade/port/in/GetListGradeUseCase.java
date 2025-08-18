package com.openschool.education.grade.port.in;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.grade.Grade;

public interface GetListGradeUseCase {

    /**
     * Get list of grades with pagination
     */
    PageResult<Grade> getListGrade(PageInfo pageInfo);
}
