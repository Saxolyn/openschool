package com.openschool.academic.port.in;

import com.openschool.academic.port.in.command.UpdateAcademicYearCommand;
import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.academic.AcademicYear;

public interface GetListAcademicYearUseCase {
    PageResult<AcademicYear> getList(PageInfo pageInfo);
}
