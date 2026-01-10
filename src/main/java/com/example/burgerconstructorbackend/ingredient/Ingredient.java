package com.example.burgerconstructorbackend.ingredient;

import com.example.burgerconstructorbackend.order_ingredient.OrderIngredient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "ingredients")
public class Ingredient {
    @Id
    @UuidGenerator
    private UUID id;

    private String name;
    private String type;

    private int proteins;
    private int fat;
    private int carbohydrates;
    private int calories;

    private double price;

    private String image;
    private String imageLarge;
    private String imageMobile;

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderIngredient> orderIngredients;
}
