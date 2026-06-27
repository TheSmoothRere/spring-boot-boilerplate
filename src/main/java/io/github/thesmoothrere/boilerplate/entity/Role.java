package io.github.thesmoothrere.boilerplate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a role entity in the system.
 * <p>
 * This entity maps to the {@code roles} table in the {@code auth} schema.
 * Roles define permissions and are assigned to users via a many-to-many relationship.
 * </p>
 *
 * @author TheSmoothRere
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "roles", schema = "auth")
public class Role {
/**
     * Unique identifier for the role.
     * Generated automatically using UUID strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false)
    private UUID id;

    /**
     * Name of the role.
     * Must be unique and up to 60 characters.
     * Common values: {@link RoleConstants#USER}, {@link RoleConstants#ADMIN}.
     */
    @Column(name = "name", length = 60, nullable = false, unique = true)
    private String name;

    /**
     * Description of the role's purpose and permissions.
     */
    @Column(name = "description", columnDefinition = "text", nullable = false)
    private String description;

    /**
     * Set of users assigned to this role.
     * Mapped by the {@code roles} field in {@link User}.
     * Uses lazy loading for performance.
     */
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    /**
     * Timestamp when the role was created.
     * Generated automatically on insert by the database.
     */
    @Generated(event = {EventType.INSERT})
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    /**
     * Timestamp when the role was last updated.
     * Generated automatically on insert and update by the database.
     */
    @Generated(event = {EventType.INSERT, EventType.UPDATE})
    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    /**
     * Checks equality based on the role ID.
     *
     * @param o the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role role)) return false;
        return Objects.equals(getId(), role.getId());
    }

    /**
     * Returns hash code based on the class.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
