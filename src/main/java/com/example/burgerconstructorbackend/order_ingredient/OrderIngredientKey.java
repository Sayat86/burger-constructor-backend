package com.example.burgerconstructorbackend.order_ingredient;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderIngredientKey implements Serializable {
    private UUID orderId;
    private UUID ingredientId;
}

