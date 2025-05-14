package com.example.didyouknow.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {
    private final String secret = "your_jwt_secret_key"; // 환경변수로 관리 권장
    private final Algorithm algorithm = Algorithm.HMAC256(secret);

    public String generateToken(String email, String name) {
        return JWT.create()
                .withSubject(email)
                .withClaim("name", name)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1시간
                .sign(algorithm);
    }

    public boolean validateToken(String token) {
        try {
            JWT.require(algorithm).build().verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
