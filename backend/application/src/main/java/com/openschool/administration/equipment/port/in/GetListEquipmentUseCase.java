package com.openschool.administration.equipment.port.in;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.equipment.Equipment;

public interface GetListEquipmentUseCase {
    PageResult<Equipment> getListEquipment(PageInfo pageInfo);
}
