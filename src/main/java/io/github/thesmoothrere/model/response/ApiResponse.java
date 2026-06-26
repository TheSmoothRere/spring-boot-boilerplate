package io.github.thesmoothrere.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

import java.time.Instant;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"success", "message", "timestamp", "data", "error"})
public class ApiResponse<T> {
    private boolean success;
    private String message;
    @Builder.Default
    private Instant timestamp = Instant.now();
    private T data;
    private ApiError error;

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(String code, String message, @Nullable Object errors) {
        return ApiResponse.<T>builder()
                .success(false)
                .error(ApiError.builder()
                        .code(code)
                        .details(message)
                        .errors(errors)
                        .build())
                .build();
    }

    public static <T> ApiResponse<T> error(String code, String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .error(ApiError.builder()
                        .code(code)
                        .details(message)
                        .build())
                .build();
    }

    @Getter
    @Builder
    @JsonPropertyOrder({"code", "details"})
    public static class ApiError {
        private String code;
        private String details;
        private Object errors;
    }
}
