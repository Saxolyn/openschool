package com.openschool.administration.equipment.port.in.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecordRepairCommand {
    private UUID maintenanceId;
    private Double actualCost;
    private String partsUsed;
    private String materialsUsed;
    private String repairNotes;
}
