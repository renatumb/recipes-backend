package org.example.recipesbackend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.example.recipesbackend.exception.RecipeNotFoundException;
import org.example.recipesbackend.model.Recipe;
import org.example.recipesbackend.model.User;
import org.example.recipesbackend.repository.RecipeRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private RecipeRespository recipeRespository;

    @Override
    public Recipe createRecipe(Recipe recipe, User user) {
        Recipe recipeCreated = new Recipe();

        recipeCreated.setCreatedAt(LocalDateTime.now());
        recipeCreated.setDescription(recipe.getDescription());
        recipeCreated.setImage(recipe.getImage());
        recipeCreated.setTitle(recipe.getTitle());
        recipeCreated.setUser(user);
        recipeCreated.setLikes( new ArrayList<Long>());
        recipeCreated.setVegetarian(recipe.getVegetarian());

        return recipeRespository.save(recipeCreated);
    }

    @Override
    public Recipe findRecipeById(Long recipeId) {
        return recipeRespository.findById(recipeId).orElseThrow(() -> new RecipeNotFoundException(recipeId));
    }

    @Override
    public void deleteRecipe(Long recipeId) {
        recipeRespository.deleteById(recipeId);
    }

    @Override
    public Recipe updateRecipe(Recipe recipe, Long recipeId) {
        Recipe oldRecipe = recipeRespository.findById(recipeId).orElseThrow(() -> new RecipeNotFoundException(recipeId));

        if (recipe.getTitle() != null) {
            oldRecipe.setTitle(recipe.getTitle());
        }

        if (recipe.getDescription() != null) {
            oldRecipe.setDescription(recipe.getDescription());
        }

        if (recipe.getImage() != null) {
            oldRecipe.setImage(recipe.getImage());
        }

        return recipeRespository.save(oldRecipe);
    }

    @Override
    public Page<Recipe> findAllRecipes(int page, int size, String sort, String[] properties) {
        return recipeRespository.findAll(PageRequest.of(page, size, Sort.Direction.fromString(sort), properties));
    }

    @Override
    public Recipe likeRecipe(Long recipeId, User user) {

        Recipe found = recipeRespository.findById(recipeId).orElseThrow(() -> new RecipeNotFoundException(recipeId));

        List<Long> likes = found.getLikes();

        if (likes.contains(user.getId())) {
            likes.remove(user.getId());
        } else {
            likes.add(user.getId());
        }

        return recipeRespository.save(found);
    }
}
