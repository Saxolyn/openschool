package com.openschool.administration.equipment.port.in;

import com.openschool.administration.equipment.port.in.command.UpdateEquipmentCommand;
import com.openschool.domain.equipment.Equipment;

public interface UpdateEquipmentDetailsUseCase {
    Equipment updateEquipmentDetails(UpdateEquipmentCommand command);
}
