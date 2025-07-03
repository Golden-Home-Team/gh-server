package kr.co.goldenhome;

import org.springframework.http.ResponseEntity;

public interface OidcClient {
    SocialPlatform getSocialPlatform();
    ResponseEntity<Void> getAuthorizationCode();
    UserInfo getUserInfo(String authorizeCode);
}
