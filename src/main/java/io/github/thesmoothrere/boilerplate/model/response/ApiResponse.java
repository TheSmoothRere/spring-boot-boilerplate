package io.github.thesmoothrere.boilerplate.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;

import java.time.Instant;

/**
 * Standardized API response wrapper for all REST endpoints.
 * <p>
 * Provides a consistent response structure with success status, message,
 * timestamp, data payload, and error details.
 * </p>
 * <p>
 * The response is automatically wrapped by {@link io.github.thesmoothrere.boilerplate.advice.RestResponseAdvice}
 * for all {@code @RestController} endpoints.
 * </p>
 *
 * @param <T> the type of the data payload
 * @author TheSmoothRere
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"success", "message", "timestamp", "data", "error"})
public class ApiResponse<T> {
    /**
     * Indicates whether the request was successful.
     */
    private boolean success;

    /**
     * Human-readable message describing the result.
     */
    private String message;

    /**
     * Timestamp when the response was generated.
     * Defaults to the current instant.
     */
    @Builder.Default
    private Instant timestamp = Instant.now();

    /**
     * The response data payload.
     * Present only on successful responses.
     */
    private T data;

    /**
     * Error details for failed responses.
     * Present only when {@code success} is {@code false}.
     */
    private ApiError error;

    /**
     * Creates a successful response with data and message.
     *
     * @param data    the response data
     * @param message the success message
     * @param <T>     the data type
     * @return a successful ApiResponse
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.message = message;
        response.data = data;
        return response;
    }

    /**
     * Creates an error response with code, message, and optional error details.
     *
     * @param code    the error code
     * @param message the error message
     * @param errors  additional error details (e.g., validation errors)
     * @param <T>     the data type (typically Void for errors)
     * @return an error ApiResponse
     */
    public static <T> ApiResponse<T> error(String code, String message, @Nullable Object errors) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = false;
        response.error = ApiError.builder()
                .code(code)
                .details(message)
                .errors(errors)
                .build();
        return response;
    }

    /**
     * Creates an error response with code and message only.
     *
     * @param code    the error code
     * @param message the error message
     * @param <T>     the data type (typically Void for errors)
     * @return an error ApiResponse
     */
    public static <T> ApiResponse<T> error(String code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = false;
        response.error = ApiError.builder()
                .code(code)
                .details(message)
                .build();
        return response;
    }

    /**
     * Error details container.
     */
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonPropertyOrder({"code", "details"})
    public static class ApiError {
        /**
         * Machine-readable error code.
         */
        private String code;

        /**
         * Human-readable error description.
         */
        private String details;

        /**
         * Additional error context (e.g., field validation errors).
         */
        private Object errors;
    }
}
