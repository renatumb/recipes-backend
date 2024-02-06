package org.example.recipesbackend.repository;

import java.util.Optional;
import org.example.recipesbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
