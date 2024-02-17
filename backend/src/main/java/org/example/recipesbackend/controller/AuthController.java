package org.example.recipesbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.example.recipesbackend.model.User;
import org.example.recipesbackend.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@Tag(name = "User registration/login", description = "API for login and new user registration")
public class AuthController {

    @Autowired
    AuthenticationService authenticationService;

    @Operation(summary = "Use this endpoint for User Creation, the 'USER' object must be provided")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully created", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "409", description = "Id or Email already in use", useReturnTypeSchema = false)})
    @PostMapping("/signup")
    public ResponseEntity<Map> signup(@RequestBody User user) {
        log.debug(">>>>>    AuthController.createUser() ");

        User noviceUser = authenticationService.signUp(user);
        String token = authenticationService.signIn(user.getEmail(), user.getPassword());

        Map dataResponse = new HashMap();
        dataResponse.put("token", token);
        dataResponse.put("message", "Signup successfully");

        return ResponseEntity.created(URI.create("/users/" + noviceUser.getId())).body(dataResponse);
    }

    @Operation(summary = "Login with username(email) and password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully LoggedIn", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "401", description = "Authentication failed", useReturnTypeSchema = false)})
    @PostMapping("/login")
    public ResponseEntity<Map> login(@NotNull @RequestBody LoginData loginData) {
        log.debug(">>>>>    AuthController.login() ");
        if (Objects.isNull(loginData.email) || Objects.isNull(loginData.password)) {
            throw new BadCredentialsException("");
        }

        Map dataResponse = new HashMap();
        dataResponse.put("token", authenticationService.signIn(loginData.email, loginData.password));
        dataResponse.put("message", "Login successfully");

        return ResponseEntity.ok(dataResponse);
    }

    private record LoginData(String email, String password) {
    }
}
