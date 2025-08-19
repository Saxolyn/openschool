package com.openschool.administration.equipment.port.in;

import java.util.UUID;

public interface DeleteEquipmentUseCase {
    void deleteEquipment(UUID equipmentId);
}
