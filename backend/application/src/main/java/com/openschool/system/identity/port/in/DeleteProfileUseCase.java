package com.openschool.system.identity.port.in;

import java.util.UUID;

public interface DeleteProfileUseCase {

    /**
     * Delete profile
     */
    void deleteProfile(UUID profileId);
}
