package com.example.burgerconstructorbackend.order.dto;

import java.util.List;

public record OrdersByNumberResponse(
        boolean success,
        List<OrderDto> orders
) {}

