package com.example.burgerconstructorbackend.ingredient.dto;

import java.util.List;

public record IngredientsResponse(
        boolean success,
        List<IngredientDto> data
) {}

