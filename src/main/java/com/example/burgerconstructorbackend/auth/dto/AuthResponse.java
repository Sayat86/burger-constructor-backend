package com.example.burgerconstructorbackend.auth.dto;

import com.example.burgerconstructorbackend.user.dto.UserResponseDto;
import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthResponse(
        boolean success,
        @JsonProperty("refresh_token") String refreshToken,
        @JsonProperty("access_token") String accessToken,
        UserResponseDto user
) {}
