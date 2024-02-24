package org.example.recipesbackend.service;

import jakarta.transaction.Transactional;
import org.example.recipesbackend.config.jwt.JwtProvider;
import org.example.recipesbackend.exception.ResourceAlreadyExistsException;
import org.example.recipesbackend.exception.UserNotFoundException;
import org.example.recipesbackend.model.User;
import org.example.recipesbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Transactional(rollbackOn = Exception.class)
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    public User createUser(User u) {
        userRepository.findByEmail(u.getEmail()).ifPresent(user -> {
            throw new ResourceAlreadyExistsException();
        });

        return userRepository.save(u);
    }

    public void removeUser(Long id) {
        userRepository.deleteById(id);
    }

    public Page<User> getAllUsers(int page, int size, String sort, String[] properties) {
        return userRepository.findAll(PageRequest.of(page, size, Sort.Direction.fromString(sort), properties));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(0L));
    }
}
