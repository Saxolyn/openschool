package com.openschool.education.student.port.in;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.student.Guardian;

public interface SearchGuardianUseCase {
    
    /**
     * Search guardian by criteria
     */
    PageResult<Guardian> searchGuardian(String searchTerm, PageInfo pageInfo);
}
