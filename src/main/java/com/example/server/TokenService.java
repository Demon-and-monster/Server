package com.example.server;

import com.example.server.request.LoginRequest;
import com.example.server.response.LoginResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Service
public class TokenService {
    private final Key key;

    public TokenService(Key theKey) {
        this.key = theKey;
    }

    @PostConstruct
    private void init() {
    }

    public LoginResponse createToken(LoginRequest request) {
        return new LoginResponse(createAccessToken(request.getUsername()));
    }

    private String createAccessToken(String username) {
        // 有效時間（毫秒）
        long expirationMillis = Instant.now()
                .plusSeconds(90)
                .getEpochSecond()
                * 1000;

        // 設置標準內容與自定義內容
        Claims claims = Jwts.claims();
        claims.setSubject("Access Token");
        claims.setIssuedAt(new Date());
        claims.setExpiration(new Date(expirationMillis));
        claims.put("username", username);

        // 簽名後產生 token
        return Jwts.builder()
                .setClaims(claims)
                .signWith(key)
                .compact();
    }
}
