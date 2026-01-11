package com.example.burgerconstructorbackend.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record UserDto(
        @JsonProperty("_id") UUID id,
        String email,
        String name
) {}

