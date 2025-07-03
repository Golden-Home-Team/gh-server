package kr.co.goldenhome;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Locator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoJwkLocator implements Locator<Key> {

    private final KakaoOidcJwkParser kakaoOidcJwkParser;
    private final OidcPublicKeyProvider oidcPublicKeyProvider;

    @Override
    public Key locate(Header header) {
        String publicKeyId = (String) header.get("kid");
        if (publicKeyId == null) {
            log.error("공개키가 존재하지 않음, origin={}", "KakaoJwkLocator.locate");
            throw new SocialLoginException();
        }
        return oidcPublicKeyProvider.get(publicKeyId, kakaoOidcJwkParser);
    }
}
