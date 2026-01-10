package com.example.burgerconstructorbackend.auth;

import com.example.burgerconstructorbackend.user.dto.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        // логика регистрации
        return new AuthResponse(true, "refresh", "access", new UserDto(request.email(), request.name()));
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        // логика логина
        return new AuthResponse(true, "refresh", "access", new UserDto("email@example.com", "John"));
    }

    @PostMapping("/token")
    public AuthResponse refresh(@RequestBody TokenRequest request) {
        // логика обновления токена
        return new AuthResponse(true, "newRefresh", "newAccess", null);
    }

    @PostMapping("/logout")
    public Map<String, Object> logout(@RequestBody TokenRequest request) {
        // логика выхода
        return Map.of("success", true);
    }
}
