package com.example.burgerconstructorbackend.auth.dto;

import com.example.burgerconstructorbackend.user.dto.UserDto;

public record AuthResponse(boolean success,
                           String refreshToken,
                           String accessToken,
                           UserDto user) {
}
