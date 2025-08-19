package com.openschool.administration.tuitionFee.port.out;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.tuition.TuitionFee;

import java.util.Optional;
import java.util.UUID;

public interface TuitionFeeRepositoryPort {
    TuitionFee create(TuitionFee tuitionFee);
    TuitionFee update(TuitionFee tuitionFee);
    Optional<TuitionFee> findById(UUID id);
    PageResult<TuitionFee> findAll(PageInfo pageInfo);
    PageResult<TuitionFee> findByStudentId(UUID studentId, PageInfo pageInfo);
    boolean delete(UUID id);
}
