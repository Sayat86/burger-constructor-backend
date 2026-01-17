package com.example.burgerconstructorbackend.order.dto;

import com.example.burgerconstructorbackend.order.model.Order;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class OrderMapper {

    private static final DateTimeFormatter ISO =
            DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public OrderDto toDto(Order order) {
        return new OrderDto(
                order.getId(),
                order.getStatus(),
                order.getName(),
                order.getCreatedAt().format(ISO),
                order.getUpdatedAt().format(ISO),
                order.getNumber(),
                order.getOrderIngredients().stream()
                        .map(oi -> oi.getIngredient().getId())
                        .distinct()
                        .toList()
        );
    }
}

