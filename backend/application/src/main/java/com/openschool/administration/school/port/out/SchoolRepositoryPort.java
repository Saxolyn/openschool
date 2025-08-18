package com.openschool.administration.school.port.out;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.school.School;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SchoolRepositoryPort {

    School create(School school);

    School update(School school);

    Optional<School> findById(UUID schoolId);

    Optional<School> findByName(String name);

    List<School> findAll();

    PageResult<School> findAll(PageInfo pageInfo);

    PageResult<School> search(String searchTerm, PageInfo pageInfo);

    boolean existsByName(String name);

    boolean existsByEmail(String email);

    boolean delete(UUID schoolId);

    long count();
}
