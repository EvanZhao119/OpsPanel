package com.opspanel.framework.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * JWT Token provider with support for both access and refresh tokens.
 */
@Component
public class JwtTokenProvider {

    @Value("${security.jwt.secret}")
    private String secretKey;

    @Value("${security.jwt.access-token-expiration-ms:900000}") // 15 min
    private long accessTokenValidity;

    @Value("${security.jwt.refresh-token-expiration-ms:604800000}") // 7 days
    private long refreshTokenValidity;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /** Generate access token. */
    public String generateAccessToken(String username, Map<String, Object> claims) {
        return generateToken(username, claims, accessTokenValidity);
    }

    /** Generate refresh token (long-term). */
    public String generateRefreshToken(String username) {
        return generateToken(username, Map.of("type", "refresh"), refreshTokenValidity);
    }

    private String generateToken(String subject, Map<String, Object> claims, long validityMs) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityMs);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /** Validate token signature and expiration. */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /** Extract username from token. */
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    /** Extract all claims. */
    public Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    /** Check if a token is a refresh token. */
    public boolean isRefreshToken(String token) {
        Object type = getClaims(token).get("type");
        return "refresh".equals(type);
    }
}
