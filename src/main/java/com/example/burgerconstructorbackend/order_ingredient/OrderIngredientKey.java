package com.example.burgerconstructorbackend.order_ingredient;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@Embeddable
public class OrderIngredientKey implements Serializable {
    private UUID orderId;
    private UUID ingredientId;

}

