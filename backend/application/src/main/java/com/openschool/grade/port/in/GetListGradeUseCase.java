package com.openschool.grade.port.in;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.grade.Grade;
import com.openschool.grade.port.in.command.UpdateGradeCommand;

import java.util.UUID;

public interface GetListGradeUseCase {
    PageResult<Grade> getListGrade(PageInfo pageInfo);
}
