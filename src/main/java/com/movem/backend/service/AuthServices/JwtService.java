package com.movem.backend.service.AuthServices;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration}")
    private long jwtExpiration;

    private static final long TRUST_TOKEN_EXPIRATION = 3 * 24 * 60 * 60 * 1000L; // 3 days

    // ===== ACCESS TOKEN =====
    public String generateToken(String username, LocalDateTime passwordChangedAt) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "ACCESS_TOKEN");
        claims.put("pwdChangedAt", passwordChangedAt != null ? passwordChangedAt.toString() : "null");

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey())
                .compact();
    }

    // ===== TRUST TOKEN =====
    public String generateTrustToken(String username, LocalDateTime passwordChangedAt) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "TRUST_TOKEN");
        claims.put("pwdChangedAt", passwordChangedAt != null ? passwordChangedAt.toString() : "null");

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TRUST_TOKEN_EXPIRATION))
                .signWith(getSignInKey())
                .compact();
    }

    public boolean isTrustToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return "TRUST_TOKEN".equals(claims.get("type"));
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractPasswordChangedAtClaim(String token) {
        return extractClaim(token, claims -> claims.get("pwdChangedAt", String.class));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}