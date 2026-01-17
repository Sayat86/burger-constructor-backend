package com.example.burgerconstructorbackend.ingredient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.UUID;

public record IngredientDto(
        @JsonProperty("_id") UUID id,
        String name,
        String type,
        int proteins,
        int fat,
        int carbohydrates,
        int calories,
        BigDecimal price,
        String image,
        @JsonProperty("image_large") String imageLarge,
        @JsonProperty("image_mobile") String imageMobile
) {}

