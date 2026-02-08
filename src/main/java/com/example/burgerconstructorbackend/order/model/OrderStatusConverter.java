package com.example.burgerconstructorbackend.order.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class OrderStatusConverter implements AttributeConverter<OrderStatus, String> {

    @Override
    public String convertToDatabaseColumn(OrderStatus status) {
        return status == null ? null : status.getValue();
    }

    @Override
    public OrderStatus convertToEntityAttribute(String value) {
        return value == null ? null : OrderStatus.fromValue(value);
    }
}
