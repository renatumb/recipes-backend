package org.example.recipesbackend.service;

import org.example.recipesbackend.exception.RecipeNotFoundException;
import org.example.recipesbackend.model.Recipe;
import org.example.recipesbackend.model.User;
import org.springframework.data.domain.Page;

public interface RecipeService {

    Recipe createRecipe(Recipe recipe, User user);

    Recipe findRecipeById(Long recipeId) throws RecipeNotFoundException;

    void deleteRecipe(Long recipeId);

    Recipe updateRecipe(Recipe recipe, Long recipeId) throws RecipeNotFoundException;

    Page<Recipe> findAllRecipes(int page, int size, String sort, String[] properties);

    Recipe likeRecipe(Long recipeId, User user) throws RecipeNotFoundException;
}
