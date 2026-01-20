package com.example.burgerconstructorbackend.order.service;

import com.example.burgerconstructorbackend.order.dto.CreateOrderRequest;
import com.example.burgerconstructorbackend.order.dto.CreateOrderResponse;
import com.example.burgerconstructorbackend.order.dto.OrdersByNumberResponse;
import com.example.burgerconstructorbackend.order.dto.OrdersResponse;
import com.example.burgerconstructorbackend.user.entity.User;

import java.util.UUID;

public interface OrderService {
    OrdersResponse getAllOrders();
    OrdersResponse getMyOrders(UUID userId);
    OrdersByNumberResponse getOrdersByNumber(int number);
    CreateOrderResponse createOrder(CreateOrderRequest request, User user);
}
