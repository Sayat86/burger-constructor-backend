package com.example.burgerconstructorbackend.auth.controller;

import com.example.burgerconstructorbackend.auth.dto.*;
import com.example.burgerconstructorbackend.auth.service.AuthService;
import com.example.burgerconstructorbackend.common.dto.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/user")
    public UserWrapperResponse getUser(Authentication authentication) {
        return authService.getUser(authentication);
    }

    @PatchMapping("/user")
    public UserWrapperResponse updateUser(@RequestBody UpdateUserRequest request,
                                          Authentication authentication) {
        return authService.updateUser(request, authentication);
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/token")
    public AuthResponse refresh(@RequestBody TokenRequest request) {
        return authService.refresh(request);
    }

    @PostMapping("/logout")
    public SuccessResponse logout(@RequestBody TokenRequest request,
                                  Authentication authentication) {
        return authService.logout(request, authentication);
    }
}
