package org.justme.articlestest.data.repository;

import org.justme.articlestest.data.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByName(String name);

    Boolean existsByName(String name);
}
