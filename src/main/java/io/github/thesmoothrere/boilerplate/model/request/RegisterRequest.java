package io.github.thesmoothrere.boilerplate.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @Size(min = 3, max = 100, message = "Username must be between 3 and 100 characters")
        @NotBlank(message = "Username is required")
        String username,

        @Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters")
        @NotBlank(message = "Password is required")
        String password
) {
}
