package kr.co.goldenhome;

import io.jsonwebtoken.io.Parser;
import io.jsonwebtoken.lang.Supplier;
import io.jsonwebtoken.security.JwkSet;
import io.jsonwebtoken.security.Jwks;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


@Component
public class KakaoOidcJwkParser implements Supplier<JwkSet> {

    @Override
    public JwkSet get() {
        RestClient restClient = RestClient.create("https://kauth.kakao.com/.well-known/jwks.json");
        String response = restClient.get().retrieve().body(String.class);
        Parser<JwkSet> jwkParser = Jwks.setParser().build();
        return jwkParser.parse(response);
    }
}
