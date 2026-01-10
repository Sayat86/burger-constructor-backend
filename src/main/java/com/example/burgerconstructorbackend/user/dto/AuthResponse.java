package com.example.burgerconstructorbackend.user.dto;

public record AuthResponse(boolean success,
                           String refreshToken,
                           String accessToken,
                           UserDto user) {
}
