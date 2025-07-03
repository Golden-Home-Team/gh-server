package kr.co.goldenhome;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoLogin implements OidcClient {

    @Value("${KAKAO_CLIENT_ID}")
    private String KAKAO_CLIENT_ID;

    @Value("${KAKAO_CLIENT_SECRET}")
    private String KAKAO_CLIENT_SECRET;

    @Value("${KAKAO_REDIRECT_URI}")
    private String KAKAO_REDIRECT_URI;

    private final OidcIdTokenParser oidcIdTokenParser;
    private final KakaoJwkLocator kakaoJwkLocator;
    private static final String ISSUER = "https://kauth.kakao.com";

    @Override
    public SocialPlatform getSocialPlatform() {
        return SocialPlatform.KAKAO;
    }

    @Override
    public ResponseEntity<Void> getAuthorizationCode() {
        String url = UriComponentsBuilder.fromUriString("https://kauth.kakao.com/oauth/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", KAKAO_CLIENT_ID)
                .queryParam("redirect_uri", KAKAO_REDIRECT_URI)
                .queryParam("scope", "openid")
                .build()
                .toUriString();
        return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, url).build();
    }

    @Override
    public UserInfo getUserInfo(String authorizeCode) {
        RestClient restClient = RestClient.create("https://kauth.kakao.com/oauth/token");
        KakaoTokenResponse response = restClient.post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(generateRequestBody(authorizeCode))
                .retrieve()
                .toEntity(KakaoTokenResponse.class)
                .getBody();
        if (response == null || response.id_token() == null) {
            log.error("access_token, id_token 요청 중 에러, origin={}", "KakaoLogin.getUserInfo");
            throw new SocialLoginException();
        }
        Claims payload = oidcIdTokenParser.parse(kakaoJwkLocator, ISSUER, response.id_token());
        String providerId = (String) payload.get("sub");
        String username = (String) payload.get("nickname");
        return new UserInfo(providerId, username);
    }

    private MultiValueMap<String, String> generateRequestBody(String authorizeCode) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("grant_type", "authorization_code");
        queryParams.add("client_id", KAKAO_CLIENT_ID);
        queryParams.add("client_secret", KAKAO_CLIENT_SECRET);
        queryParams.add("redirect_uri", KAKAO_REDIRECT_URI);
        queryParams.add("code", authorizeCode);
        return queryParams;
    }
}
