package com.example.burgerconstructorbackend.order.dto;

import java.util.List;

public record OrdersResponse(
        boolean success,
        List<OrderDto> orders,
        int total,
        int totalToday
) {}

