package io.github.thesmoothrere.boilerplate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.generator.EventType;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a user entity in the system.
 * <p>
 * This entity maps to the {@code users} table in the {@code auth} schema.
 * It contains user credentials, status, roles, and audit timestamps.
 * </p>
 *
 * @author TheSmoothRere
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "users", schema = "auth")
public class User {
    /**
     * Unique identifier for the user.
     * Generated automatically using UUID strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false)
    private UUID id;

    /**
     * Username of the user.
     * Must be unique and between 3-100 characters.
     */
    @Column(name = "username", length = 100, nullable = false, unique = true)
    private String username;

    /**
     * Encrypted password of the user.
     * Stored as text, never in plain text.
     */
    @Column(name = "password", columnDefinition = "text", nullable = false)
    private String password;

    /**
     * Current status of the user account.
     * Defaults to {@link UserStatus#ACTIVE}.
     */
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    /**
     * Set of roles assigned to this user.
     * Mapped through the {@code user_roles} join table in the {@code auth} schema.
     * Uses lazy loading for performance.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            schema = "auth",
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    /**
     * Timestamp when the user was created.
     * Generated automatically on insert by the database.
     */
    @Generated(event = {EventType.INSERT})
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    /**
     * Timestamp when the user was last updated.
     * Generated automatically on insert and update by the database.
     */
    @Generated(event = {EventType.INSERT, EventType.UPDATE})
    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    /**
     * Checks equality based on the user ID.
     *
     * @param o the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getId(), user.getId());
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
