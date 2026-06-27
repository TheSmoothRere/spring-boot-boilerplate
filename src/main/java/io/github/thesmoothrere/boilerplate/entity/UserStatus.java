package io.github.thesmoothrere.boilerplate.entity;

/**
 * Enumeration of possible user account statuses.
 * <p>
 * Used to track the lifecycle state of a user account for authentication and authorization decisions.
 * </p>
 *
 * @author TheSmoothRere
 * @since 0.0.1-SNAPSHOT
 */
public enum UserStatus {
    /**
     * User account is active and can authenticate normally.
     */
    ACTIVE,
    /**
     * User account is inactive and cannot authenticate.
     * Typically used for soft-deleted or suspended accounts.
     */
    INACTIVE,
    /**
     * User account is locked due to security reasons.
     * For example, too many failed login attempts.
     */
    LOCKED
}
