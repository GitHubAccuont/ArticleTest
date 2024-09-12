package org.justme.articlestest.data.repository;

import org.justme.articlestest.data.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(String name);

    Boolean existsByName(String name);
}
