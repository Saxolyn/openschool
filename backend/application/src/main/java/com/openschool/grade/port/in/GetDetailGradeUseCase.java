package com.openschool.grade.port.in;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.grade.Grade;

import java.util.UUID;

public interface GetDetailGradeUseCase {
    Grade getListGrade(UUID gradeId);
}
