package com.openschool.system.identity.port.out;

import com.openschool.domain.identity.model.Role;

import java.util.Optional;

public interface RoleRepositoryPort {
    Optional<Role> getRoleByName(String roleName);
}
