package io.github.thesmoothrere.boilerplate.controller;

import io.github.thesmoothrere.boilerplate.model.request.LoginRequest;
import io.github.thesmoothrere.boilerplate.model.request.RegisterRequest;
import io.github.thesmoothrere.boilerplate.model.response.AuthResponse;
import io.github.thesmoothrere.boilerplate.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @GetMapping("/status")
    public String status() {
        return "OK";
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse login(@Valid @RequestBody LoginRequest request, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        return authService.login(request, servletRequest, servletResponse);
    }
}
