package com.openschool.administration.equipment.port.in.command;

import com.openschool.domain.equipment.EquipmentCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReturnEquipmentCommand {
    private UUID loanId;
    private EquipmentCondition returnCondition;
    private String returnNotes;
}
