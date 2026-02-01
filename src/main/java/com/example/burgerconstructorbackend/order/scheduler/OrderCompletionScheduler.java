package com.example.burgerconstructorbackend.order.scheduler;

import com.example.burgerconstructorbackend.order.model.Order;
import com.example.burgerconstructorbackend.order.model.OrderStatus;
import com.example.burgerconstructorbackend.order.repository.OrderRepository;
import com.example.burgerconstructorbackend.order.service.OrderCompletionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCompletionScheduler {

    private final OrderRepository orderRepository;
    private final OrderCompletionService orderCompletionService;

    @Scheduled(cron = "1 * * * * *")
    public void checkPendingOrders() {
        LocalDateTime twoMinutesAgo = LocalDateTime.now().minusMinutes(2);
        
        List<Order> ordersToComplete = orderRepository.findByStatusAndCreatedAtBefore(OrderStatus.PENDING, twoMinutesAgo);
        
        if (!ordersToComplete.isEmpty()) {
            log.info("Found {} pending orders to complete", ordersToComplete.size());
            
            ordersToComplete.forEach(order -> {
                try {
                    orderCompletionService.completeOrder(order.getId());
                } catch (Exception e) {
                    log.error("Failed to complete order {}", order.getId(), e);
                }
            });
        }
    }
}
