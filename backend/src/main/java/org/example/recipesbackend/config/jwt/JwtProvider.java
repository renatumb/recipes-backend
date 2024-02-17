package org.example.recipesbackend.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtProvider {

    private static final String SECRET_KEY = "498542f879b3c26081899a3751ac0942f92a06e756390f05ce1c8142740c6762";
    private final SecretKey secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(Authentication auth) {

        String token = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + (1000 * 60 * 60 * 5))) // 1 hour after
                .setSubject(auth.getName())
                .signWith(secretKey)
                .compact();

        return token;
    }

    public Claims extractAllClaims(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }

    public String getEmailFromJwtToken(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        Claims claims = extractAllClaims(token);

        final boolean valid = (!claims.getExpiration().before(new Date()))
                && claims.getSubject().equals(userDetails.getUsername());

        return valid;
    }
}
