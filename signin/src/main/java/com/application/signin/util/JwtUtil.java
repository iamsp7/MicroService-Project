package com.application.signin.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET = "YourSuperSecretKeyMustBeAtLeast32Chars!";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    // 1 minute (60 seconds)
    private static final long EXPIRATION_TIME = 10 * 60 * 1000; // 10 minutes


    // Generates a token with username (subject) and role
    public static String generateToken(String subject, String role) {
        return Jwts.builder()
                .setSubject(subject)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract username (subject)
    public static String extractSubject(String token) throws ExpiredJwtException, JwtException {
        return parseClaims(token).getSubject();
    }

    // Extract role
    public static String extractRole(String token) throws JwtException {
        return parseClaims(token).get("role", String.class);
    }

    // Internal: parses claims and validates the token
    private static Claims parseClaims(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
