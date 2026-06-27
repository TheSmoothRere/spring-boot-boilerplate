package io.github.thesmoothrere.boilerplate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession(
        redisNamespace = "spring-boot-boilerplate"
)
public class SessionConfig {
}
