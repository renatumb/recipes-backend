package org.example.recipesbackend.service;

import lombok.extern.slf4j.Slf4j;
import org.example.recipesbackend.config.jwt.CustomUserDetailsService;
import org.example.recipesbackend.config.jwt.JwtProvider;
import org.example.recipesbackend.exception.ResourceAlreadyExistsException;
import org.example.recipesbackend.model.User;
import org.example.recipesbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User signUp(User user) {
        log.debug(">>>>>    AuthenticationServiceImpl.signUp() ");
        userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new ResourceAlreadyExistsException();
        });

        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setId(userRepository.save(newUser).getId());

        return newUser;
    }

    public String signIn(String email, String password) {
        log.debug(">>>>>    AuthenticationServiceImpl.signIn() ");
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException(email);
        }
        return authenticate(userDetails.getUsername(), userDetails.getPassword());
    }

    private String authenticate(String userName, String password) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(userName, password, null);
        return jwtProvider.generateToken(authentication);
    }
}
