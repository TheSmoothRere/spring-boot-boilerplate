package io.github.thesmoothrere.advice;

import io.github.thesmoothrere.model.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import tools.jackson.databind.ObjectMapper;

/**
 * Global Response Body Wrapper.
 * <p>
 * Automatically wraps all REST controller responses in the standardized ApiResponse
 * envelope before serialization. This ensures consistent response format across all
 * endpoints.
 * </p>
 * <p>
 * Response wrapping behavior:
 * <ul>
 *   <li>ApiResponse objects: returned as-is (not double-wrapped)</li>
 *   <li>null body: wrapped with success=true and null data</li>
 *   <li>String body: wrapped as JSON string response</li>
 *   <li>Other objects: wrapped with success=true and object as data</li>
 * </ul>
 * </p>
 *
 * @see ApiResponse
 * @see RestExceptionAdvice
 * @author TheSmoothRere
 * @since 1.0
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class RestResponseAdvice implements ResponseBodyAdvice<Object> {
    /**
     * Default success message.
     */
    public static final String OPERATION_SUCCESSFUL = "Operation successful";

    /**
     * Jackson ObjectMapper for JSON serialization.
     */
    private final ObjectMapper objectMapper;

    /**
     * Indicates whether this advice should handle a response.
     * <p>
     * Returns true for all responses to enable universal wrapping.
     * </p>
     *
     * @param returnType the controller method return type
     * @param converterType the selected message converter type
     * @return true to process this response
     * @since 1.0
     */
    @Override
    @NullMarked
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    /**
     * Wrap the response body before serialization.
     * <p>
     * Handles four scenarios:
     * <ol>
     *   <li>Already wrapped (ApiResponse): return as-is</li>
     *   <li>Null body: wrap with success message</li>
     *   <li>String body: serialize as JSON string response</li>
     *   <li>Other objects: wrap with success and data</li>
     * </ol>
     * </p>
     *
     * @param body the response body from the controller
     * @param returnType the controller method return type
     * @param selectedContentType the selected content type
     * @param selectedConverterType the selected converter type
     * @param request the HTTP request
     * @param response the HTTP response
     * @return the wrapped response body (or original if already wrapped)
     * @since 1.0
     */
    @Override
    @NullMarked
    public @Nullable Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof ApiResponse) {
            return body;
        }

        if (body == null) {
            return ApiResponse.success(null, OPERATION_SUCCESSFUL);
        }

        if (body instanceof String) {
            try {
                return objectMapper.writeValueAsString(ApiResponse.success(body, OPERATION_SUCCESSFUL));
            } catch (Exception _) {
                return "{\"success\":false,\"error\":{\"code\":\"SERIALIZATION_ERROR\",\"details\":\"Failed to serialize string response\"}}";
            }
        }

        return ApiResponse.success(body, OPERATION_SUCCESSFUL);

    }
}
