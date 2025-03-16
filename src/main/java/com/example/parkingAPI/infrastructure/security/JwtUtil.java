
package com.example.parkingAPI.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtil {
    private static final Dotenv dotenv = Dotenv.configure().load();
    
    private static final String SECRET_KEY = dotenv.get("SECRET_KEY");
    private static final long EXPIRATION_TIME = Long.parseLong(dotenv.get("TOKEN_EXPIRATION"));

    public String generateToken(String email, String profileId, String role) {
        return JWT.create()
                .withSubject(email)
                .withClaim("profileId", profileId) // ID do perfil ativo
                .withClaim("role", role) // Role do perfil ativo
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    public String generateTokenLogin(String email, String id) {
        return JWT.create()
                .withSubject(email)
                .withSubject(id)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    public String extractEmail(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build()
                .verify(token)
                .getSubject();
    }

    public String extractUserId(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build()
                .verify(token)
                .getSubject();
    }

    public String extractProfileId(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build()
                .verify(token)
                .getClaim("profileId").asString();
    }

    public String extractRole(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build()
                .verify(token)
                .getClaim("role").asString();
    }

    public boolean validateToken(String token) {
        try {
            JWT.require(Algorithm.HMAC256(SECRET_KEY)).build().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }
}
