package io.github.thesmoothrere.boilerplate.entity;

/**
 * Constants for role names used throughout the application.
 * <p>
 * This utility class defines standard role identifiers that are used for
 * authorization and access control. Role names are stored in the database
 * and referenced by Spring Security for method-level and URL-based security.
 * </p>
 * <p>
 * <strong>Note:</strong> This class should not be instantiated.
 * </p>
 *
 * @author TheSmoothRere
 * @since 0.0.1-SNAPSHOT
 */
public final class RoleConstants {
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private RoleConstants() {
        /* This utility class should not be instantiated */
    }

    /**
     * Standard user role.
     * <p>
     * Grants basic access to authenticated endpoints.
     * Assigned to all newly registered users by default.
     * </p>
     */
    public static final String USER = "USER";

    /**
     * Administrator role.
     * <p>
     * Grants elevated privileges including administrative operations.
     * Should be assigned sparingly.
     * </p>
     */
    public static final String ADMIN = "ADMIN";
}
