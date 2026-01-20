package com.example.burgerconstructorbackend.ingredient.controller;

import com.example.burgerconstructorbackend.ingredient.dto.IngredientsResponse;
import com.example.burgerconstructorbackend.ingredient.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    @GetMapping
    public IngredientsResponse getAll() {
        return ingredientService.getAllIngredients();
    }
}

