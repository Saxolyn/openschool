package com.openschool.administration.equipment.port.in;

import com.openschool.administration.equipment.port.in.command.ScheduleMaintenanceCommand;
import com.openschool.domain.equipment.EquipmentMaintenance;

public interface ScheduleMaintenanceUseCase {
    EquipmentMaintenance scheduleMaintenance(ScheduleMaintenanceCommand command);
}
