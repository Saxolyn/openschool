package com.openschool.administration.tuitionFee.port.in;

import com.openschool.administration.tuitionFee.port.in.command.GenerateTuitionInvoiceCommand;
import com.openschool.domain.tuition.TuitionFee;

public interface GenerateTuitionInvoiceUseCase {
    TuitionFee generateTuitionInvoice(GenerateTuitionInvoiceCommand command);
}
