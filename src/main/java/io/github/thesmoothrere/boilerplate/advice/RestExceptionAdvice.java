package io.github.thesmoothrere.boilerplate.advice;

import io.github.thesmoothrere.boilerplate.model.response.ApiResponse;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler for REST controllers.
 * <p>
 * Maps exceptions to standardized {@link ApiResponse} error responses
 * with appropriate HTTP status codes.
 * </p>
 * <p>
 * Exception handling hierarchy:
 * </p>
 * <ul>
 *   <li>{@code MethodArgumentNotValidException} -> 400 Bad Request (validation errors)</li>
 *   <li>{@code EntityExistsException} -> 409 Conflict</li>
 *   <li>{@code EntityNotFoundException} -> 404 Not Found</li>
 *   <li>{@code UsernameNotFoundException} -> 401 Unauthorized (generic message)</li>
 *   <li>{@code BadCredentialsException} -> 401 Unauthorized</li>
 *   <li>{@code AccessDeniedException} -> 403 Forbidden</li>
 *   <li>{@code Exception} -> 500 Internal Server Error (catch-all)</li>
 * </ul>
 * <p>
 * Note: Authentication exceptions return generic messages to prevent
 * username enumeration attacks.
 * </p>
 *
 * @author TheSmoothRere
 * @since 0.0.1-SNAPSHOT
 */
@Slf4j
@RestControllerAdvice
public class RestExceptionAdvice {
    /**
     * Handles all unhandled exceptions.
     *
     * @param e the exception
     * @return error response with 500 status
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleException(Exception e) {
        log.error("An unexpected error occurred", e);
        return ApiResponse.error("INTERNAL_SERVER_ERROR", "An unexpected error occurred on the server.");
    }

    /**
     * Handles validation errors from {@code @Valid} annotated parameters.
     *
     * @param e the validation exception
     * @return error response with 400 status and field errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> fieldErrors = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value",
                        (existing, replacement) -> existing + ", " + replacement
                ));

        return ApiResponse.error("VALIDATION_ERROR", "Validation failed", fieldErrors);
    }

    /**
     * Handles entity already exists conflicts.
     *
     * @param e the entity exists exception
     * @return error response with 409 status
     */
    @ExceptionHandler(EntityExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse<Void> handleEntityExistsException(EntityExistsException e) {
        return ApiResponse.error("ENTITY_EXISTS", e.getMessage());
    }

    /**
     * Handles entity not found errors.
     *
     * @param e the entity not found exception
     * @return error response with 404 status
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleEntityNotFoundException(EntityNotFoundException e) {
        return ApiResponse.error("ENTITY_NOT_FOUND", e.getMessage());
    }

    /**
     * Handles user not found during authentication.
     * Returns generic message to prevent username enumeration.
     *
     * @param e the username not found exception
     * @return error response with 401 status
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<Void> handleUsernameNotFoundException(UsernameNotFoundException e) {
        // Use a generic message to prevent username enumeration attacks
        return ApiResponse.error("UNAUTHORIZED", "Invalid credentials.");
    }

    /**
     * Handles bad credentials during authentication.
     *
     * @param e the bad credentials exception
     * @return error response with 401 status
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<Void> handleBadCredentialsException(BadCredentialsException e) {
        return ApiResponse.error("UNAUTHORIZED", "Invalid credentials.");
    }

    /**
     * Handles access denied errors (authorization failures).
     *
     * @param e the access denied exception
     * @return error response with 403 status
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<Void> handleAccessDeniedException(AccessDeniedException e) {
        return ApiResponse.error("ACCESS_DENIED", "You do not have permission to access this resource.");
    }
}
