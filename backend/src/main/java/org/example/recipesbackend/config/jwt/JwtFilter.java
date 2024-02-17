package org.example.recipesbackend.config.jwt;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug(">>>>>    JwtFilter.doFilterInternal() ");

        MAIN_IF:
        if (request.getServletPath().matches("(/users.*)|(/recipe.*)")) {
            log.debug("Matched filter");

            String authHeader = request.getHeader("Authorization");
            String email;
            String token;

            if (Objects.isNull(authHeader) || !authHeader.startsWith("Bearer ")) {
                break MAIN_IF;
            }
            token = authHeader.substring(7);
            email = jwtProvider.getEmailFromJwtToken(token);

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

            if (!jwtProvider.isTokenValid(token, userDetails)) {
                break MAIN_IF;
            }
            Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
