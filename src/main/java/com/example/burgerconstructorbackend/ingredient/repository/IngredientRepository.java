package com.example.burgerconstructorbackend.ingredient.repository;

import com.example.burgerconstructorbackend.ingredient.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IngredientRepository extends JpaRepository<Ingredient, UUID> {
    List<Ingredient> findByType(String type);
}
