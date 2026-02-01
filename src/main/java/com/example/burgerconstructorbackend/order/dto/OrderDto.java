package com.example.burgerconstructorbackend.order.dto;

import com.example.burgerconstructorbackend.order.model.OrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public record OrderDto(
        @JsonProperty("_id") UUID id,
        OrderStatus status,
        String name,
        String createdAt,
        String updatedAt,
        int number,
        List<UUID> ingredients
) {}

