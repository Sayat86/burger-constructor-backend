package com.example.burgerconstructorbackend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RegisterRequest(
        @NotBlank
        @Email(message = "Некорректный формат email")
        String email,
        @NotBlank
        String name,
        @NotBlank
        String password) {
}
