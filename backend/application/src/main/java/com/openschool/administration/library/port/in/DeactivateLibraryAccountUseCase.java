package com.openschool.administration.library.port.in;

import java.util.UUID;

public interface DeactivateLibraryAccountUseCase {
    void deactivateLibraryAccount(UUID userId);
}
