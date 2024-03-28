package org.example.recipesbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.recipesbackend.exception.RecipeNotFoundException;
import org.example.recipesbackend.model.Recipe;
import org.example.recipesbackend.service.RecipeService;
import org.example.recipesbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Recipe", description = "Recipe API ")
@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserService userService;

    @Operation(summary = "Recipe creation", description = "Create a recipe", parameters = {@Parameter(name = "userid", description = "Id of User who is creating the recipe")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully created"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping()
    public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe) {
        log.debug(">>>>>    RecipeController.createRecipe()");
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok().body(recipeService.createRecipe(recipe, userService.getUserByEmail(email) ));
    }

    @Operation(summary = "Retrieve all recipes", parameters = {
            @Parameter(name = "page", description = "Page Number where starting with 0", example = "2"),
            @Parameter(name = "size", description = "Quantity of record per page"),
            @Parameter(name = "sort", description = "How to sort the records ASC/DESC ", examples = {@ExampleObject(name = "Ascendant", value = "ASC"), @ExampleObject(name = "Descendant", value = "DESC")}),
            @Parameter(name = "fields", description = "Sortable columns: id, description, title, user_id. Use comma when utilizing more than one")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paged available recipes"),
            @ApiResponse(responseCode = "401", description = "Not Authorized")
    })
    @GetMapping
    public ResponseEntity<Page<Recipe>> findAllRecipes(@RequestParam(value = "page", defaultValue = "0") int page,
                                                       @RequestParam(value = "size", defaultValue = "5") int size,
                                                       @RequestParam(value = "sort", defaultValue = "asc") String sort,
                                                       @RequestParam(value = "fields", defaultValue = "id") String properties) {
        log.debug(">>>>>    RecipeController.findAllRecipes()");
        return ResponseEntity.ok().body(recipeService.findAllRecipes(page, size, sort, properties.split(",")));
    }

    @Operation(summary = "Retrieve a recipe by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recipe found for the given ID"),
            @ApiResponse(responseCode = "401", description = "Not Authorized"),
            @ApiResponse(responseCode = "404", description = "NO Recipe found for the given ID")
    })
    @GetMapping("/{recipeId}")
    public ResponseEntity<Recipe> findRecipeById(@PathVariable Long recipeId) {
        log.debug(">>>>>    RecipeController.findRecipeById()");
        return ResponseEntity.ok(recipeService.findRecipeById(recipeId));

    }

    @DeleteMapping("/{recipeId}")
    @Operation(summary = "Use this endpoint for Recipe Removal", description = "Delete recipe")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successfully removed"),
            @ApiResponse(responseCode = "401", description = "Not Authorized"),
    })

    public ResponseEntity deleteRecipe(@PathVariable Long recipeId) {
        log.debug(">>>>>    RecipeController.deleteRecipe()");
        recipeService.deleteRecipe(recipeId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update a recipe", description = "Update a recipe")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paged available recipes"),
            @ApiResponse(responseCode = "401", description = "Not Authorized"),
            @ApiResponse(responseCode = "404", description = "Recipe not found with given ID")
    })
    @PutMapping("/{recipeId}")
    public ResponseEntity<Recipe> updateRecipe(@RequestBody Recipe recipe, @PathVariable Long recipeId) {
        log.debug(">>>>>    RecipeController.updateRecipe()");
        return ResponseEntity.ok(recipeService.updateRecipe(recipe, recipeId));
    }

    @Operation(summary = "Like/Dislike a recipe", description = "Like/Dislike a recipe")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully liked"),
            @ApiResponse(responseCode = "404", description = "Recipe/User NOT found for the given ID")
    })
    @PutMapping("/{recipeID}/like")
    public ResponseEntity<Recipe> likeRecipe(@PathVariable(name = "recipeID") Long recipeId) {
        log.debug(">>>>>    RecipeController.likeRecipe()");
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(recipeService.likeRecipe(recipeId, userService.getUserByEmail(email)));
    }
}
