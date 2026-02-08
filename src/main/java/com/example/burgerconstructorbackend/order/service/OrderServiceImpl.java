package com.example.burgerconstructorbackend.order.service;

import com.example.burgerconstructorbackend.ingredient.model.Ingredient;
import com.example.burgerconstructorbackend.ingredient.repository.IngredientRepository;
import com.example.burgerconstructorbackend.order.dto.*;
import com.example.burgerconstructorbackend.order.model.Order;
import com.example.burgerconstructorbackend.order.model.OrderStatus;
import com.example.burgerconstructorbackend.order.repository.OrderRepository;
import com.example.burgerconstructorbackend.order_ingredient.OrderIngredient;
import com.example.burgerconstructorbackend.order_ingredient.OrderIngredientKey;
import com.example.burgerconstructorbackend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final IngredientRepository ingredientRepository;

    @Override
    @Transactional(readOnly = true)
    public OrdersResponse getAllOrders() {
        List<Order> orders = orderRepository.findAllByOrderByCreatedAtDesc();

        int total = orders.size();

        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        int totalToday = (int) orderRepository.countCreatedBetween(start, end);

        List<OrderDto> dtos = orders.stream()
                .map(orderMapper::toDto)
                .toList();

        return new OrdersResponse(true, dtos, total, totalToday);
    }

    @Override
    @Transactional(readOnly = true)
    public OrdersResponse getMyOrders(UUID userId) {
        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);

        int total = orders.size();

        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        int totalToday = (int) orderRepository.countUserCreatedBetween(userId, start, end);

        List<OrderDto> dtos = orders.stream()
                .map(orderMapper::toDto)
                .toList();

        return new OrdersResponse(true, dtos, total, totalToday);
    }

    @Override
    @Transactional(readOnly = true)
    public OrdersByNumberResponse getOrdersByNumber(int number) {
        Order order = orderRepository.findByNumber(number)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        OrderDto dto = orderMapper.toDto(order);

        // Swagger требует "orders": [ ... ] даже для одного заказа
        return new OrdersByNumberResponse(true, List.of(dto));
    }

    @Override
    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest request, User user) {
        if (request == null || request.ingredients() == null || request.ingredients().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ingredients must not be empty");
        }

        Map<UUID, Long> counts = request.ingredients().stream()
                .collect(Collectors.groupingBy(id -> id, Collectors.counting()));

        List<Ingredient> ingredients = ingredientRepository.findAllById(counts.keySet());

        if (ingredients.size() != counts.size()) {
            Set<UUID> found = ingredients.stream().map(Ingredient::getId).collect(Collectors.toSet());
            List<UUID> missing = counts.keySet().stream()
                    .filter(id -> !found.contains(id))
                    .toList();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown ingredient ids: " + missing);
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setName(user.getName());

        for (Ingredient ing : ingredients) {
            OrderIngredient oi = new OrderIngredient();

            OrderIngredientKey key = new OrderIngredientKey();
            key.setOrderId(order.getId());
            key.setIngredientId(ing.getId());
            oi.setId(key);

            oi.setOrder(order);
            oi.setIngredient(ing);
            oi.setQuantity(counts.get(ing.getId()).intValue());

            order.getOrderIngredients().add(oi);
        }

        Order saved = orderRepository.save(order);

        Order withIngredients = orderRepository.findWithIngredientsById(saved.getId())
                .orElse(saved);

        OrderDto dto = orderMapper.toDto(withIngredients);
        return new CreateOrderResponse(true, dto, dto.name());
    }

}
