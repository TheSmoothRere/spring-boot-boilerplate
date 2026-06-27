package io.github.thesmoothrere.service;

import io.github.thesmoothrere.entity.Role;
import io.github.thesmoothrere.entity.RoleConstants;
import io.github.thesmoothrere.entity.User;
import io.github.thesmoothrere.entity.UserStatus;
import io.github.thesmoothrere.model.request.LoginRequest;
import io.github.thesmoothrere.model.request.RegisterRequest;
import io.github.thesmoothrere.model.response.AuthResponse;
import io.github.thesmoothrere.repository.RoleRepository;
import io.github.thesmoothrere.repository.UserRepository;
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

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

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
