package com.example.burgerconstructorbackend.order.model;

import com.example.burgerconstructorbackend.order_ingredient.OrderIngredient;
import com.example.burgerconstructorbackend.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(nullable = false)
    private String status;
    private String name;

    @Column(nullable = false, unique = true, insertable = false, updatable = false)
    private int number;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderIngredient> orderIngredients = new ArrayList<>();
}
