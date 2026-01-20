package com.example.burgerconstructorbackend.order.dto;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(
        List<UUID> ingredients
) {}

