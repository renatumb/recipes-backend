package org.example.recipesbackend.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.example.recipesbackend.model.User;
import org.example.recipesbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "User", description = "The User API ")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Use this endpoint for User Creation, the 'USER' object must be provided")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully created", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "409", description = "Id or Email already in use", useReturnTypeSchema = false)})
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User created = userService.createUser(user);
        return ResponseEntity.created(URI.create("/user/" + created.getId())).body(created);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Use this endpoint for User Removal")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successfully removed"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity removeUser(@PathVariable Long id) {
        userService.removeUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "All Users retrieval", description = "Use this endpoint to retrieve all the users. It is sortable thru User properties",
            parameters = {
                    @Parameter(name = "page", description = "Page Number where starting with 0", example = "2"),
                    @Parameter(name = "size", description = "Quantity of record per page"),
                    @Parameter(name = "sort", description = "How to sort the records", examples = {@ExampleObject(name = "Ascendant", value = "ASC"), @ExampleObject(name = "Descendant", value = "DESC")}),
                    @Parameter(name = "fields", description = "Sortable columns: id, lastName, firstName, email. Use comma when utilizing more than one",
                            examples = {@ExampleObject(name = "Order by id", value = "id"), @ExampleObject(name = "Order by First Name", value = "firstName"), @ExampleObject(name = "Order by First name then Last name", value = "firstName,lastName")})})

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success", useReturnTypeSchema = false),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
    public ResponseEntity<Page<User>> getAllUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "size", defaultValue = "5") int size,
                                                  @RequestParam(value = "sort", defaultValue = "asc") String sort,
                                                  @RequestParam(value = "fields", defaultValue = "id") String properties) {
        return ResponseEntity.ok().body(userService.getAllUsers(page, size, sort, properties.split(",")));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve user by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found for the given ID"),
    })
    public ResponseEntity<User> getUser(@NotNull @PathVariable(value = "id") Long id) {
        return ResponseEntity.ok().body(userService.getUserById(id));
    }

    @GetMapping("/byEmail/{email}")
    public ResponseEntity<User> getUserByEmail(@NotNull @PathVariable(value = "email") String email) {
        return ResponseEntity.ok().body(userService.getUserByEmail(email)  );
    }

}
