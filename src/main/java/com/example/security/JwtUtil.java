package com.example.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

	private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	
    private final long expirationMs = 3600000; // 1h 
  
    public String generateToken(UserDetails userDetails, Integer userId) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
    }
    
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
    
    public Jws<Claims> validateToken2(String token) throws JwtException {
        return Jwts.parserBuilder()
                   .setSigningKey(key)
                   .build()
                   .parseClaimsJws(token);
    }
    
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    
    public Integer getUserIdFromToken(String token) {
        Object claim = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId");
        if (claim instanceof Integer) {
            return (Integer) claim;
        } else if (claim instanceof String) {
            return Integer.valueOf((String) claim);
        }
        return null;
    }
    
    public Integer getPlayerIdFromToken(String token) {
        Jws<Claims> claims = validateToken2(token);
        return claims.getBody().get("playerId", Integer.class);
    }
}