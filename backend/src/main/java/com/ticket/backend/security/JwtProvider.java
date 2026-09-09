package com.ticket.backend.security;

import com.ticket.backend.domain.Users;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

    private final String secret =
            "ticker-project-secret-key-ticket-project-2026";

    private final SecretKey key =
            Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

    private final long accessTokenExpiration = 1000L * 60 * 30;
    private final long refreshTokenExpiration = 1000L * 60 * 60 * 24 * 7;

    //로그인 시 API요청 -> 토큰 발급
    public String createAccessToken(Users users) {

        Date now = new Date();
        Date expiration = new Date(now.getTime() + accessTokenExpiration);

        return Jwts.builder()
                .subject(String.valueOf(users.getUserId()))
                .claim("role", users.getRole())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    //토큰 만료시 RefreshToken으로 새로 발급
    public String createRefreshToken(Users users) {

        Date now = new Date();
        Date expiration =
                new Date(now.getTime() + refreshTokenExpiration);

        return Jwts.builder()
                .subject(String.valueOf(users.getUserId()))
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    //토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

