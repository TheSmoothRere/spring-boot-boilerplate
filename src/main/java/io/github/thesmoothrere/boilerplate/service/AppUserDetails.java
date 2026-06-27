package io.github.thesmoothrere.boilerplate.service;

import io.github.thesmoothrere.boilerplate.entity.Role;
import io.github.thesmoothrere.boilerplate.entity.User;
import io.github.thesmoothrere.boilerplate.entity.UserStatus;
import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Spring Security {@link UserDetails} implementation for the application.
 * <p>
 * Wraps a {@link User} entity and provides the security framework with
 * authentication and authorization information including authorities,
 * account status, and credentials.
 * </p>
 *
 * @author TheSmoothRere
 * @since 0.0.1-SNAPSHOT
 */
@Getter
public class AppUserDetails implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier of the user.
     */
    private final UUID id;

    /**
     * Username of the user.
     */
    private final String username;

    /**
     * Encrypted password of the user.
     */
    private final String password;

    /**
     * Current status of the user account.
     */
    private final UserStatus status;

    /**
     * Authorities granted to the user.
     */
    private final Set<GrantedAuthority> authorities;

    /**
     * Creates user details from a User entity.
     *
     * @param user the user entity to wrap
     */
    public AppUserDetails(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.status = user.getStatus();

        this.authorities = user.getRoles()
                .stream()
                .map(Role::getName)
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Returns the authorities granted to the user.
     * Roles are prefixed with {@code ROLE_} as per Spring Security convention.
     *
     * @return the collection of granted authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    /**
     * Returns the user's password (encrypted).
     *
     * @return the password
     */
    @Override
    public @Nullable String getPassword() {
        return this.password;
    }

    /**
     * Returns the username.
     *
     * @return the username
     */
    @Override
    public String getUsername() {
        return this.username;
    }

    /**
     * Checks if the account is not expired.
     * Always returns {@code true} as expiration is not implemented.
     *
     * @return {@code true}
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Checks if the account is not locked.
     * Returns {@code false} if status is {@link UserStatus#LOCKED}.
     *
     * @return {@code true} if account is not locked
     */
    @Override
    public boolean isAccountNonLocked() {
        return this.status != UserStatus.LOCKED;
    }

    /**
     * Checks if credentials are not expired.
     * Always returns {@code true} as credential expiration is not implemented.
     *
     * @return {@code true}
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Checks if the account is enabled.
     * Returns {@code true} only if status is {@link UserStatus#ACTIVE}.
     *
     * @return {@code true} if account is active
     */
    @Override
    public boolean isEnabled() {
        return this.status == UserStatus.ACTIVE;
    }
}
