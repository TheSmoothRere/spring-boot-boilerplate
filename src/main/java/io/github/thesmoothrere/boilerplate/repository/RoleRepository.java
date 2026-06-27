package io.github.thesmoothrere.boilerplate.repository;

import io.github.thesmoothrere.boilerplate.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for {@link Role} entities.
 * <p>
 * Provides standard CRUD operations plus a custom query to find a role by name.
 * </p>
 *
 * @author TheSmoothRere
 * @since 0.0.1-SNAPSHOT
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    /**
     * Finds a role by its name.
     *
     * @param name the role name to search for (e.g., {@link io.github.thesmoothrere.boilerplate.entity.RoleConstants#USER})
     * @return an Optional containing the role if found
     */
    Optional<Role> findByName(String name);
}
