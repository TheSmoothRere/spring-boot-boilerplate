package io.github.thesmoothrere.repository;

import io.github.thesmoothrere.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
