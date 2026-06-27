package io.github.thesmoothrere.boilerplate.service;

import io.github.thesmoothrere.boilerplate.entity.Role;
import io.github.thesmoothrere.boilerplate.entity.RoleConstants;
import io.github.thesmoothrere.boilerplate.entity.User;
import io.github.thesmoothrere.boilerplate.entity.UserStatus;
import io.github.thesmoothrere.boilerplate.model.request.LoginRequest;
import io.github.thesmoothrere.boilerplate.model.request.RegisterRequest;
import io.github.thesmoothrere.boilerplate.model.response.AuthResponse;
import io.github.thesmoothrere.boilerplate.repository.RoleRepository;
import io.github.thesmoothrere.boilerplate.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

/**
 * Service for authentication operations: registration and login.
 * <p>
 * Handles user registration with default USER role assignment,
 * and login with session-based authentication using Spring Security.
 * </p>
 *
 * @author TheSmoothRere
 * @since 0.0.1-SNAPSHOT
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    /**
     * Registers a new user with the default USER role.
     *
     * @param request the registration request containing username and password
     * @return the authentication response with user ID and username
     * @throws EntityExistsException if a user with the username already exists
     * @throws EntityNotFoundException if the default USER role is not configured
     */
    @Transactional
    public AuthResponse register(@NonNull RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new EntityExistsException("User with username: " + request.username() + " already exists.");
        }

        Role userRole = roleRepository.findByName(RoleConstants.USER)
                .orElseThrow(() -> new EntityNotFoundException("Default USER role not found"));

        User user = new User();
        user.setUsername(request.username());
        user.setStatus(UserStatus.ACTIVE);
        user.setPassword(passwordEncoder.encode(request.password()));

        user.getRoles().add(userRole);

        userRepository.save(user);

        return AuthResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }

    /**
     * Authenticates a user and establishes a session.
     *
     * @param request the login request containing username and password
     * @param servletRequest the HTTP servlet request for session handling
     * @param servletResponse the HTTP servlet response for session handling
     * @return the authentication response with user ID, username, and roles
     * @throws org.springframework.security.authentication.BadCredentialsException if authentication fails
     */
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(
                        request.username(),
                        request.password()
                )
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);

        securityContextRepository.saveContext(context, servletRequest, servletResponse);

        AppUserDetails principal = (AppUserDetails) authentication.getPrincipal();

        return AuthResponse.builder()
                .id(principal.getId())
                .username(principal.getUsername())
                .roles(principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()))
                .build();
    }
}
