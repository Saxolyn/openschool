package com.openschool.administration.equipment.port.in;

import java.util.UUID;

public interface AddEquipmentToInventoryUseCase {
    void addEquipmentToInventory(UUID equipmentId, Integer quantity);
}
