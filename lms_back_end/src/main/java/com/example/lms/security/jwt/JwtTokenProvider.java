package com.example.lms.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException; // මෙය import කරන්න
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        String token = Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
        System.out.println("JwtTokenProvider: Generated Token for " + userPrincipal.getUsername() + ", first 20 chars: " + token.substring(0, Math.min(token.length(), 20))); // New log
        return token;
    }

    private Key key() {
        // System.out.println("JwtTokenProvider: Raw secret from application.yml: " + jwtSecret); // This was previous debug
        try {
            return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        } catch (IllegalArgumentException e) {
            System.err.println("JwtTokenProvider: ERROR - Invalid JWT secret for Base64 decoding: " + e.getMessage());
            throw new RuntimeException("Invalid JWT secret, check application.yml", e);
        }
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        System.out.println("JwtTokenProvider: Validating JWT: " + authToken.substring(0, Math.min(authToken.length(), 20)) + "..."); // New log
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            System.out.println("JwtTokenProvider: JWT validated successfully."); // New log
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            System.out.println("JwtTokenProvider: JWT Validation FAILED (MalformedJwtException): " + e.getMessage()); // New log
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
            System.out.println("JwtTokenProvider: JWT Validation FAILED (ExpiredJwtException): " + e.getMessage()); // New log
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
            System.out.println("JwtTokenProvider: JWT Validation FAILED (UnsupportedJwtException): " + e.getMessage()); // New log
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
            System.out.println("JwtTokenProvider: JWT Validation FAILED (IllegalArgumentException): " + e.getMessage()); // New log
        } catch (SignatureException e) { // <<< මෙය වැදගත්! Secret Key mismatch දෝෂය අල්ලා ගනී.
            logger.error("Invalid JWT signature: {}", e.getMessage());
            System.out.println("JwtTokenProvider: JWT Validation FAILED (SignatureException): " + e.getMessage()); // New log
        }
        return false;
    }
}