package org.example.recipesbackend.service;


import org.example.recipesbackend.model.User;

public interface AuthenticationService {

    User signUp(User user);
    String signIn(String email, String password);
    boolean validateToken(String token);
}
