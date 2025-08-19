package com.openschool.administration.library.port.in;

import java.util.UUID;

public interface SendOverdueNoticeUseCase {
    void sendOverdueNotice(UUID borrowerId);
}
