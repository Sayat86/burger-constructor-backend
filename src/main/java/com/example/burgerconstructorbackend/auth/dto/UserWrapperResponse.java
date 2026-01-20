package com.example.burgerconstructorbackend.auth.dto;

import com.example.burgerconstructorbackend.user.dto.UserResponseDto;

public record UserWrapperResponse(
        boolean success,
        UserResponseDto user
) {}

