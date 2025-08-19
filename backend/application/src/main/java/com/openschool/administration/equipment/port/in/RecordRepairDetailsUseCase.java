package com.openschool.administration.equipment.port.in;

import com.openschool.administration.equipment.port.in.command.RecordRepairCommand;
import com.openschool.domain.equipment.EquipmentMaintenance;

public interface RecordRepairDetailsUseCase {
    EquipmentMaintenance recordRepairDetails(RecordRepairCommand command);
}
