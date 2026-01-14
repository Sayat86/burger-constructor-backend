package com.example.burgerconstructorbackend.auth.dto;

public record UpdateUserRequest(
        String email,
        String name,
        String password
) {}

