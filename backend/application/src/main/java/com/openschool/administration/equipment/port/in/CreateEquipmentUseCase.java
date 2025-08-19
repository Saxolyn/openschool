package com.openschool.administration.equipment.port.in;

import com.openschool.administration.equipment.port.in.command.CreateEquipmentCommand;
import com.openschool.domain.equipment.Equipment;

public interface CreateEquipmentUseCase {
    Equipment createEquipment(CreateEquipmentCommand command);
}
