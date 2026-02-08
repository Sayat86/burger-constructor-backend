package com.example.burgerconstructorbackend.common.error;

import lombok.Builder;

@Builder
public record ErrorResponse(
        boolean success,
        String message
) {}

