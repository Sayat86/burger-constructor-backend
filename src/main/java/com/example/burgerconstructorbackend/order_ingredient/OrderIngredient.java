package com.example.burgerconstructorbackend.order_ingredient;

import com.example.burgerconstructorbackend.ingredient.Ingredient;
import com.example.burgerconstructorbackend.order.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_ingredients")
public class OrderIngredient {
    @EmbeddedId
    private OrderIngredientKey id;

    @ManyToOne
    @MapsId("orderId") // связывает часть составного ключа с Order
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @MapsId("ingredientId") // связывает часть составного ключа с Ingredient
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @Column(nullable = false)
    private int quantity;
}
