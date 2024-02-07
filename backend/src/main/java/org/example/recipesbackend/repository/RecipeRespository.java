package org.example.recipesbackend.repository;

import org.example.recipesbackend.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRespository extends JpaRepository<Recipe, Long> {
}
