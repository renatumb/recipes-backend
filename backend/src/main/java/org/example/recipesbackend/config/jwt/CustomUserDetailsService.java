package org.example.recipesbackend.config.jwt;

import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.example.recipesbackend.model.User;
import org.example.recipesbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        log.debug(">>>>>    CustomUserDetailsService.loadUserByUsername() ");
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> {
            throw new UsernameNotFoundException(userEmail);
        });
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList());
    }
}
