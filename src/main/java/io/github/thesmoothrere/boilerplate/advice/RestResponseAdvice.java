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

@RestControllerAdvice
@RequiredArgsConstructor
public class RestResponseAdvice implements ResponseBodyAdvice<Object> {
    public static final String OPERATION_SUCCESSFUL = "Operation successful";
    private final ObjectMapper objectMapper;

    @Override
    @NullMarked
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getDeclaringClass().isAnnotationPresent(RestController.class);
    }

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
