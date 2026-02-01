package com.example.burgerconstructorbackend.order.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderStatus {
    CREATED("created"),
    PENDING("pending"),
    DONE("done");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static OrderStatus fromValue(String value) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + value);
    }
}
