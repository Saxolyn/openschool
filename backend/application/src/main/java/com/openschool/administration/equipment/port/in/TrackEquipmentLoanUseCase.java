package com.openschool.administration.equipment.port.in;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.equipment.EquipmentLoan;
import java.util.UUID;

public interface TrackEquipmentLoanUseCase {
    PageResult<EquipmentLoan> trackEquipmentLoan(PageInfo pageInfo);
    EquipmentLoan trackEquipmentLoanById(UUID loanId);
}
