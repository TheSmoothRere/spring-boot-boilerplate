package io.github.thesmoothrere.boilerplate.model.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for user login.
 * <p>
 * Contains the credentials required for authentication.
 * Both username and password are required and validated.
 * </p>
 *
 * @param username the username (required)
 * @param password the password (required)
 * @author TheSmoothRere
 * @since 0.0.1-SNAPSHOT
 */
public record LoginRequest(
        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Password is required")
        String password
) {
}
