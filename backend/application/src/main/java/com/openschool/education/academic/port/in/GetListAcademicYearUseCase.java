package com.openschool.education.academic.port.in;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.academic.AcademicYear;

public interface GetListAcademicYearUseCase {
    PageResult<AcademicYear> getList(PageInfo pageInfo);
}
