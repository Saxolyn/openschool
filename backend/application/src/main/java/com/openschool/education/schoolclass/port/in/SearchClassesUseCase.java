package com.openschool.education.schoolclass.port.in;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.schoolclass.model.SchoolClass;

public interface SearchClassesUseCase {

    /**
     * Search classes by criteria
     */
    PageResult<SchoolClass> searchClasses(String searchTerm, PageInfo pageInfo);
}
