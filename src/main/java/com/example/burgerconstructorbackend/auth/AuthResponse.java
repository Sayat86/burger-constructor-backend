package com.example.burgerconstructorbackend.auth;

import com.example.burgerconstructorbackend.user.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthResponse(
        boolean success,
        @JsonProperty("refresh_token") String refreshToken,
        @JsonProperty("access_token") String accessToken,
        UserDto user
) {}

