package com.example.burgerconstructorbackend.user.dto;

public record UpdateUserRequest(
        String email,
        String name,
        String password
) {}

