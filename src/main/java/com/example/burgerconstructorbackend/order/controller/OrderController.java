package com.example.burgerconstructorbackend.order.controller;

import com.example.burgerconstructorbackend.order.dto.CreateOrderRequest;
import com.example.burgerconstructorbackend.order.dto.CreateOrderResponse;
import com.example.burgerconstructorbackend.order.dto.OrdersByNumberResponse;
import com.example.burgerconstructorbackend.order.dto.OrdersResponse;
import com.example.burgerconstructorbackend.order.service.OrderService;
import com.example.burgerconstructorbackend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/all")
    public OrdersResponse getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping
    public OrdersResponse getMyOrders(@AuthenticationPrincipal User user) {
        return orderService.getMyOrders(user.getId());
    }

    @GetMapping("/{number}")
    public OrdersByNumberResponse getByNumber(@PathVariable int number) {
        return orderService.getOrdersByNumber(number);
    }

    @PostMapping
    public CreateOrderResponse createOrder(
            @RequestBody CreateOrderRequest request,
            @AuthenticationPrincipal User user
    ) {
        return orderService.createOrder(request, user);
    }
}


