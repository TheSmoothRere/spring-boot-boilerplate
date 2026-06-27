package io.github.thesmoothrere.boilerplate.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

/**
 * Response DTO for authentication operations.
 * <p>
 * Returned after successful registration or login.
 * Contains the user's ID, username, and assigned roles.
 * </p>
 *
 * @author TheSmoothRere
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {
    /**
     * Unique identifier of the authenticated user.
     */
    private UUID id;

    /**
     * Username of the authenticated user.
     */
    private String username;

    /**
     * Set of role names assigned to the user.
     * Roles are prefixed with {@code ROLE_} as per Spring Security convention.
     */
    private Set<String> roles;
}
