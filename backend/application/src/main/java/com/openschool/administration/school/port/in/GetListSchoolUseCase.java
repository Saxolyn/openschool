package com.openschool.administration.school.port.in;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.school.School;

public interface GetListSchoolUseCase {
    
    /**
     * Get list of schools with pagination
     */
    PageResult<School> getListSchool(PageInfo pageInfo);
}
