package com.openschool.administration.tuitionFee.port.out;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.tuition.Payment;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepositoryPort {
    Payment create(Payment payment);
    Payment update(Payment payment);
    Optional<Payment> findById(UUID id);
    PageResult<Payment> findAll(PageInfo pageInfo);
    PageResult<Payment> findByStudentId(UUID studentId, PageInfo pageInfo);
    boolean delete(UUID id);
}
