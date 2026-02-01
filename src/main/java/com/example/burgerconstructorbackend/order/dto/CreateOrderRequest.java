package com.example.burgerconstructorbackend.order.dto;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record CreateOrderRequest(
        List<UUID> ingredients
) {}

