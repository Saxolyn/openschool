package com.openschool.education.teachingMaterial.port.in;

import com.openschool.education.teachingMaterial.port.in.command.CreateTeachingMaterialCommand;
import com.openschool.domain.teachingmaterial.TeachingMaterial;

public interface CreateTeachingMaterialUseCase {
    TeachingMaterial createTeachingMaterial(CreateTeachingMaterialCommand command);
}

