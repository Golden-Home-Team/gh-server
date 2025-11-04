package kr.co.goldenhome;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Locator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Clock;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class OidcIdTokenParser {

    public Claims parse(Locator<Key> locator, String issuer, String token) {
        try {
            JwtParser jwtParser = Jwts.parser()
                    .keyLocator(locator)
                    .requireIssuer(issuer)
                    .clock(() -> Date.from(Clock.systemUTC().instant()))
                    .build();
            return jwtParser.parseSignedClaims(token).getPayload();
        } catch (Exception e) {
            log.error("OIDC id_token 파싱 에러 = {}, origin={}", e.getMessage(), "OidcIdTokenParser.parse");
            throw new SocialLoginException();
        }
    }
}
