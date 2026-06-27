package io.github.thesmoothrere.boilerplate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * Spring Session configuration with Redis backend.
 * <p>
 * Enables Redis-backed HTTP sessions for distributed session management.
 * The session namespace is {@code spring-boot-boilerplate}.
 * </p>
 *
 * @author TheSmoothRere
 * @since 0.0.1-SNAPSHOT
 */
@Configuration
@EnableRedisHttpSession(
        redisNamespace = "spring-boot-boilerplate"
)
public class SessionConfig {
}
