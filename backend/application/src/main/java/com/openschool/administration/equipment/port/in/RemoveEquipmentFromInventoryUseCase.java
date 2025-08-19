package com.openschool.administration.equipment.port.in;

import java.util.UUID;

public interface RemoveEquipmentFromInventoryUseCase {
    void removeEquipmentFromInventory(UUID equipmentId, Integer quantity);
}
