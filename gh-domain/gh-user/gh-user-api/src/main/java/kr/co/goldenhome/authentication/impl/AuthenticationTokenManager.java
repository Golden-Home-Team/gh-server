package kr.co.goldenhome.authentication.impl;

import exception.CustomException;
import exception.ErrorCode;
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

    public LoginResponse refresh(String refreshToken) {
        Claims claims = validRefreshToken(refreshToken);
        Long userId = claims.get("userId", Long.class);
        String accessToken = generateAccessToken(userId);
        if (isNearExpiration(claims.getExpiration())) return new LoginResponse(accessToken, generateRefreshToken(userId));
        return new LoginResponse(accessToken, refreshToken);
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

    private Claims validRefreshToken(String refreshToken) {
        Claims claims = getClaims(refreshToken);
        if (!claims.getSubject().equals("refreshToken")) throw new CustomException(ErrorCode.UNAUTHORIZED_TOKEN, "AuthenticationTokenManager.validRefreshToken");
        Long userId = claims.get("userId", Long.class);
        String storedToken = refreshTokenRepository.getByUserId(userId);
        if (!refreshToken.equals(storedToken)) throw new CustomException(ErrorCode.UNAUTHORIZED_TOKEN, "AuthenticationTokenManager.validRefreshToken");
        return claims;
    }

    private boolean isNearExpiration(Date expiration) {
        Date now = new Date();
        long differenceInMillis = expiration.getTime() - now.getTime();
        long differenceInDays = differenceInMillis / (1000 * 60 * 60 * 24);
        return differenceInDays <= 7;
    }
}
