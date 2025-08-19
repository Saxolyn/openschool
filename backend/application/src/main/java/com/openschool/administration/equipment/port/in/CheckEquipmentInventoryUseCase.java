package com.openschool.administration.equipment.port.in;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.equipment.Equipment;
import java.util.UUID;

public interface CheckEquipmentInventoryUseCase {
    PageResult<Equipment> checkEquipmentInventory(PageInfo pageInfo);
    Equipment checkEquipmentInventoryById(UUID equipmentId);
}
