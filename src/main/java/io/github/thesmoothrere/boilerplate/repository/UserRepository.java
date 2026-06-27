package io.github.thesmoothrere.boilerplate.repository;

import io.github.thesmoothrere.boilerplate.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for {@link User} entities.
 * <p>
 * Provides standard CRUD operations plus custom queries for user lookup.
 * Uses {@code @EntityGraph} to fetch roles eagerly when needed to avoid N+1 queries.
 * </p>
 *
 * @author TheSmoothRere
 * @since 0.0.1-SNAPSHOT
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    /**
     * Finds a user by username with roles eagerly fetched.
     *
     * @param username the username to search for
     * @return an Optional containing the user if found
     */
    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findByUsername(String username);

    /**
     * Checks if a user exists with the given username.
     *
     * @param username the username to check
     * @return true if a user exists, false otherwise
     */
    boolean existsByUsername(String username);
}
