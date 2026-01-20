package com.example.burgerconstructorbackend.ingredient.dto;

import com.example.burgerconstructorbackend.ingredient.model.Ingredient;
import org.springframework.stereotype.Component;

@Component
public class IngredientMapper {
    public IngredientDto toDto(Ingredient ingredient) {
        return new IngredientDto(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getType(),
                ingredient.getProteins(),
                ingredient.getFat(),
                ingredient.getCarbohydrates(),
                ingredient.getCalories(),
                ingredient.getPrice(),
                ingredient.getImage(),
                ingredient.getImageLarge(),
                ingredient.getImageMobile()
        );
    }
}

