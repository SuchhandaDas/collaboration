package com.test.collaboration.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil {
    private final String SECRET = "MySuperSecretKeyForJWTGenerationAndValidation123456"; // min 32 chars
    private final long EXPIRATION_MS = 3600000;

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(Long employeeId) {
        return Jwts.builder()
                .setSubject(employeeId.toString())
                .setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(key).compact();
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String validateTokenAndGetEmployeeId(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
        } catch (JwtException e) {
            return null;
        }
    }
}
