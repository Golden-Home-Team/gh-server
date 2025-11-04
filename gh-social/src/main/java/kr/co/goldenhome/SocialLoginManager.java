package kr.co.goldenhome;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SocialLoginManager {

    private final List<OidcClient> oidcClients;

    public ResponseEntity<Void> getAuthorizationCode(SocialPlatform socialPlatform) {
        for (OidcClient oidcClient : oidcClients) {
            if (oidcClient.getSocialPlatform() == socialPlatform) return oidcClient.getAuthorizationCode();
        }
        return null;
    }

    public UserInfo getUserInfo(SocialPlatform socialPlatform, String authorizationCode) {
        for (OidcClient oidcClient : oidcClients) {
            if (oidcClient.getSocialPlatform() == socialPlatform) return oidcClient.getUserInfo(authorizationCode);
        }
        return null;
    }
}
