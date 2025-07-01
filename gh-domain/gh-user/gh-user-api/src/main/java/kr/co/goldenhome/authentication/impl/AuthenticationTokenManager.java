package kr.co.goldenhome.authentication.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kr.co.goldenhome.authentication.dto.LoginResponse;
import kr.co.goldenhome.infrastructure.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

@Component
public class AuthenticationTokenManager {

    private final SecretKey key;
    private final RefreshTokenRepository refreshTokenRepository;
    public static final Long ACCESS_TOKEN_VALID_TIME = 1000L * 60 * 60 * 24;
    public static final Long REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 60 * 24 * 30;

    public AuthenticationTokenManager(@Value("${JWT_SECRET_KEY}") String secretKey, RefreshTokenRepository refreshTokenRepository) {
        byte[] secretKeyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(secretKeyBytes);
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public LoginResponse create(Long userId) {
        String accessToken = generateAccessToken(userId);
        String refreshToken = generateRefreshToken(userId);
        return new LoginResponse(accessToken, refreshToken);
    }

    public Long getUserId(String accessToken) {
        Claims claims = getClaims(accessToken);
        return claims.get("userId", Long.class);
    }

    private String generateAccessToken(Long userId) {
        Claims claims = Jwts.claims().
                subject("accessToken")
                .add("userId", userId)
                .build();
        Date currentTime = new Date();

        return Jwts.builder()
                .claims(claims)
                .issuedAt(currentTime)
                .expiration(new Date(currentTime.getTime() + ACCESS_TOKEN_VALID_TIME))
                .signWith(key)
                .compact();
    }

    private String generateRefreshToken(Long userId) {
        Claims claims = Jwts.claims()
                .subject("refreshToken")
                .add("userId", userId)
                .build();
        Date currentTime = new Date();

        String refreshToken = Jwts.builder()
                .claims(claims)
                .issuedAt(currentTime)
                .expiration(new Date(currentTime.getTime() + REFRESH_TOKEN_VALID_TIME))
                .signWith(key)
                .compact();
        refreshTokenRepository.save(userId, refreshToken, Duration.ofMillis(REFRESH_TOKEN_VALID_TIME));
        return refreshToken;
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
