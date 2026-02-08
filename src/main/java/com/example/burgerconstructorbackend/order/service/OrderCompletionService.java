package com.example.burgerconstructorbackend.order.service;

import com.example.burgerconstructorbackend.order.model.OrderStatus;
import com.example.burgerconstructorbackend.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCompletionService {

    private final OrderRepository orderRepository;

    @Transactional
    public void completeOrder(UUID orderId) {
        orderRepository.findById(orderId).ifPresent(order -> {
            order.setStatus(OrderStatus.DONE);
            orderRepository.save(order);
            log.info("Order {} status changed to done", orderId);
        });
    }
}
