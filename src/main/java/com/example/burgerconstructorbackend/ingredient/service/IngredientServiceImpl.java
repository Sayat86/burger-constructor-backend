package com.example.burgerconstructorbackend.ingredient.service;

import com.example.burgerconstructorbackend.ingredient.dto.IngredientDto;
import com.example.burgerconstructorbackend.ingredient.dto.IngredientMapper;
import com.example.burgerconstructorbackend.ingredient.dto.IngredientsResponse;
import com.example.burgerconstructorbackend.ingredient.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {
    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;

    @Override
    public IngredientsResponse getAllIngredients() {
        List<IngredientDto> data = ingredientRepository.findAll().stream()
                .map(ingredientMapper::toDto)
                .toList();

        return new IngredientsResponse(true, data);
    }
}
