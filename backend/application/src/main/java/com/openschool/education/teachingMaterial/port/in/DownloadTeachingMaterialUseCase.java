package com.openschool.education.teachingMaterial.port.in;

import java.util.UUID;

public interface DownloadTeachingMaterialUseCase {
    byte[] downloadTeachingMaterial(UUID materialId);
}

