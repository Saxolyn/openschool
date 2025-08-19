package com.openschool.administration.equipment.port.in.command;

import com.openschool.domain.equipment.MaintenanceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleMaintenanceCommand {
    private UUID equipmentId;
    private MaintenanceType type;
    private String description;
    private LocalDateTime scheduledDate;
    private UUID assignedTechnicianId;
    private String externalServiceProvider;
    private Double estimatedCost;
    private String notes;
}
