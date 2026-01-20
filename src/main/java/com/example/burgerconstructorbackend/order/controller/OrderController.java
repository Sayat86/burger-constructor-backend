package com.example.burgerconstructorbackend.order.controller;

import com.example.burgerconstructorbackend.order.dto.CreateOrderRequest;
import com.example.burgerconstructorbackend.order.dto.CreateOrderResponse;
import com.example.burgerconstructorbackend.order.dto.OrdersByNumberResponse;
import com.example.burgerconstructorbackend.order.dto.OrdersResponse;
import com.example.burgerconstructorbackend.order.service.OrderService;
import com.example.burgerconstructorbackend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public OrdersResponse getMyOrders(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return orderService.getMyOrders(user.getId());
    }

    @GetMapping("/{number}")
    public OrdersByNumberResponse getByNumber(@PathVariable int number) {
        return orderService.getOrdersByNumber(number);
    }

    @PostMapping
    public CreateOrderResponse createOrder(
            @RequestBody CreateOrderRequest request,
            Authentication authentication
    ) {
        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return orderService.createOrder(request, user);
    }
}


