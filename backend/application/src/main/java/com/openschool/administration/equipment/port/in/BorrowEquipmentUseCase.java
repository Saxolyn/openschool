package com.openschool.administration.equipment.port.in;

import com.openschool.administration.equipment.port.in.command.BorrowEquipmentCommand;
import com.openschool.domain.equipment.EquipmentLoan;

public interface BorrowEquipmentUseCase {
    EquipmentLoan borrowEquipment(BorrowEquipmentCommand command);
}
