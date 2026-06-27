package io.github.thesmoothrere.boilerplate.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter to ensure CSRF token is generated and accessible to JavaScript.
 * <p>
 * Spring Security's {@link CookieCsrfTokenRepository} lazily generates the CSRF token
 * only when {@link CsrfToken#getToken()} is called. This filter triggers token generation
 * by calling {@code getToken()} on each request, ensuring the cookie is set in the response.
 * </p>
 * <p>
 * This allows Single Page Applications (SPAs) to read the CSRF token from the cookie
 * and include it in subsequent requests via the {@code X-CSRF-TOKEN} header.
 * </p>
 *
 * @author TheSmoothRere
 * @since 0.0.1-SNAPSHOT
 */
@Component
public class CsrfCookieFilter extends OncePerRequestFilter {
    @Override
    @NullMarked
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (csrfToken != null) {
            csrfToken.getToken();
        }
        filterChain.doFilter(request, response);
    }
}
