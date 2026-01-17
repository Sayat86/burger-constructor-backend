package com.example.burgerconstructorbackend.order.dto;

public record CreateOrderResponse(
        boolean success,
        OrderDto order,
        String name
) {}
