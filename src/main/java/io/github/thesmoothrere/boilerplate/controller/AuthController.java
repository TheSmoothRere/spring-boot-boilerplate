package io.github.thesmoothrere.boilerplate.controller;

import io.github.thesmoothrere.boilerplate.model.request.LoginRequest;
import io.github.thesmoothrere.boilerplate.model.request.RegisterRequest;
import io.github.thesmoothrere.boilerplate.model.response.AuthResponse;
import io.github.thesmoothrere.boilerplate.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for authentication endpoints.
 * <p>
 * Provides endpoints for user registration and login.
 * All endpoints are publicly accessible (no authentication required).
 * Responses are automatically wrapped by {@link io.github.thesmoothrere.boilerplate.advice.RestResponseAdvice}.
 * </p>
 *
 * @author TheSmoothRere
 * @since 0.0.1-SNAPSHOT
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    /**
     * Health check endpoint for the auth service.
     *
     * @return "OK" if the service is running
     */
    @GetMapping("/status")
    public String status() {
        return "OK";
    }

    /**
     * Registers a new user.
     *
     * @param request the registration request with username and password
     * @return the authentication response with user ID and username
     * @throws jakarta.persistence.EntityExistsException if username already exists
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    /**
     * Authenticates a user and creates a session.
     *
     * @param request the login request with username and password
     * @param servletRequest the HTTP request for session cookie handling
     * @param servletResponse the HTTP response for session cookie handling
     * @return the authentication response with user ID, username, and roles
     * @throws org.springframework.security.authentication.BadCredentialsException if authentication fails
     */
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse login(@Valid @RequestBody LoginRequest request, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        return authService.login(request, servletRequest, servletResponse);
    }
}
