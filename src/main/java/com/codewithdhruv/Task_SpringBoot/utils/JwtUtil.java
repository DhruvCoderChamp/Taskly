package com.codewithdhruv.Task_SpringBoot.utils;

import com.codewithdhruv.Task_SpringBoot.entities.User;
import com.codewithdhruv.Task_SpringBoot.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final UserRepository userRepository;
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(),userDetails);

    }

    private String generateToken(Map<String,Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSiningKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSiningKey() {
        byte[] keyBytes= Decoders.BASE64.decode("4djsfhdsbdfhsf8fwebfwe78fwgfbvwef87ebrew78fweb76dgd6ggdd5dd58476gg");
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName=extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);

    }

    public String extractUserName(String token) {
        return extractClaim(token,Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }

    private <T> T  extractClaim(String token, Function<Claims,T> claimsResolvers) {
        final Claims claims=extractAllClaims(token);
        return  claimsResolvers.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSiningKey()) // Set the key for signature verification
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof User) {
                return (User) principal;
            }
        }
        return null;
    }

}
