package com.openschool.education.teachingMaterial.port.in;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.teachingmaterial.TeachingMaterial;
import java.util.UUID;

public interface ViewTeachingMaterialUseCase {
    TeachingMaterial viewTeachingMaterial(UUID materialId);
    PageResult<TeachingMaterial> viewTeachingMaterialsBySubject(UUID subjectId, PageInfo pageInfo);
}

