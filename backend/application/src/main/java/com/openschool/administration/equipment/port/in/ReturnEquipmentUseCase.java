package com.openschool.administration.equipment.port.in;

import com.openschool.administration.equipment.port.in.command.ReturnEquipmentCommand;
import com.openschool.domain.equipment.EquipmentLoan;

public interface ReturnEquipmentUseCase {
    EquipmentLoan returnEquipment(ReturnEquipmentCommand command);
}
