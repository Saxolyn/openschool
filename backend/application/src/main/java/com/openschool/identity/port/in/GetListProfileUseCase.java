package com.openschool.identity.port.in;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.grade.Grade;
import com.openschool.domain.identity.model.Profile;

public interface GetListProfileUseCase {
    PageResult<Profile> getListGrade(PageInfo pageInfo);
}
