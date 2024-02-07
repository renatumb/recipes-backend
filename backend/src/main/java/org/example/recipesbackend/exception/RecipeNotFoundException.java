package org.example.recipesbackend.exception;

public class RecipeNotFoundException extends RuntimeException {
    public RecipeNotFoundException(Long id) {
        super(String.format("Recipe with Id %d not found", id));
    }
}
