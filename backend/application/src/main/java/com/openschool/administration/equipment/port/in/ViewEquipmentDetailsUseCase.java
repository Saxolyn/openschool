package com.openschool.administration.equipment.port.in;

import com.openschool.domain.equipment.Equipment;
import java.util.UUID;

public interface ViewEquipmentDetailsUseCase {
    Equipment viewEquipmentDetails(UUID equipmentId);
}
