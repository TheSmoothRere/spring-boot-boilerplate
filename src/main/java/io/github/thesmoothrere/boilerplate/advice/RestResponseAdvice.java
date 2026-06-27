package io.github.thesmoothrere.boilerplate.advice;

import io.github.thesmoothrere.boilerplate.model.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import tools.jackson.databind.ObjectMapper;

/**
 * Global response wrapper for all REST controllers.
 * <p>
 * Automatically wraps successful responses in {@link ApiResponse} format
 * for all endpoints annotated with {@code @RestController}.
 * </p>
 * <p>
 * Response wrapping rules:
 * </p>
 * <ul>
 *   <li>If body is already an {@link ApiResponse}, returns as-is</li>
 *   <li>If body is null, returns success response with null data</li>
 *   <li>If body is a String, serializes as JSON with success wrapper</li>
 *   <li>Otherwise, wraps body in success response with default message</li>
 * </ul>
 *
 * @author TheSmoothRere
 * @since 0.0.1-SNAPSHOT
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class RestResponseAdvice implements ResponseBodyAdvice<Object> {
    /**
     * Default success message for wrapped responses.
     */
    public static final String OPERATION_SUCCESSFUL = "Operation successful";

    private final ObjectMapper objectMapper;

    /**
     * Determines if this advice applies to the given return type.
     *
     * @param returnType the method return type
     * @param converterType the HTTP message converter type
     * @return true if the controller is annotated with @RestController
     */
    @Override
    @NullMarked
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getDeclaringClass().isAnnotationPresent(RestController.class);
    }

    /**
     * Wraps the response body in a standardized ApiResponse format.
     *
     * @param body the original response body
     * @param returnType the method return type
     * @param selectedContentType the selected content type
     * @param selectedConverterType the selected converter type
     * @param request the server HTTP request
     * @param response the server HTTP response
     * @return the wrapped response body
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
