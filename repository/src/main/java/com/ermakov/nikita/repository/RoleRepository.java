package com.ermakov.nikita.repository;

import com.ermakov.nikita.entity.security.Role;
import org.springframework.data.repository.Repository;

/**
 * created by Nikita_Ermakov at 2/19/2020
 */
@org.springframework.stereotype.Repository("roleRepository")
public interface RoleRepository extends Repository<Role, Long> {

    Role findByName(String name);
}
