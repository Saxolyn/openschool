package com.openschool.administration.tuitionFee.port.in;

import com.openschool.administration.tuitionFee.port.in.command.ProcessTuitionPaymentCommand;
import com.openschool.domain.tuition.Payment;

public interface ProcessTuitionPaymentUseCase {
    Payment processTuitionPayment(ProcessTuitionPaymentCommand command);
}
