package com.example.burgerconstructorbackend.common.error;

public record ErrorResponse(
        boolean success,
        String message
) {}

