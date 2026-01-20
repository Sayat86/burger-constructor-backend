package com.example.burgerconstructorbackend.order.repository;

import com.example.burgerconstructorbackend.order.model.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @EntityGraph(attributePaths = {"orderIngredients", "orderIngredients.ingredient"})
    Optional<Order> findByNumber(int number);

    @EntityGraph(attributePaths = {"orderIngredients", "orderIngredients.ingredient"})
    List<Order> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = {"orderIngredients", "orderIngredients.ingredient"})
    List<Order> findByUserIdOrderByCreatedAtDesc(UUID userId);

    @Query("""
           select count(o)
           from Order o
           where o.createdAt >= :start and o.createdAt < :end
           """)
    long countCreatedBetween(@Param("start") LocalDateTime start,
                             @Param("end") LocalDateTime end);

    @Query("""
           select count(o)
           from Order o
           where o.user.id = :userId
             and o.createdAt >= :start and o.createdAt < :end
           """)
    long countUserCreatedBetween(@Param("userId") UUID userId,
                                 @Param("start") LocalDateTime start,
                                 @Param("end") LocalDateTime end);

    @EntityGraph(attributePaths = {"orderIngredients", "orderIngredients.ingredient"})
    Optional<Order> findWithIngredientsById(UUID id);
}


