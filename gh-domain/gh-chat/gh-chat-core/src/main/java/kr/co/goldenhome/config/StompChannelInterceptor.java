package kr.co.goldenhome.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kr.co.goldenhome.auth.UserPrincipal;
import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import kr.co.goldenhome.messaging.ChatPrincipal;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;

@Slf4j
@Component
public class StompChannelInterceptor implements ChannelInterceptor {

    private final SecretKey key;

    public StompChannelInterceptor(@Value("${JWT_SECRET_KEY}") String secretKey) {
        byte[] secretKeyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(secretKeyBytes);
    }

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(message);
        if (stompHeaderAccessor.getCommand() == null) throw new CustomException(ErrorCode.INVALID_MESSAGE, "StompChannelInterceptor.preSend");
        if (stompHeaderAccessor.getCommand() == StompCommand.CONNECT) {
            String accessToken = parseToken(stompHeaderAccessor.getFirstNativeHeader("Authorization"));
            try {
                Claims claims = Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(accessToken)
                        .getPayload();
                Long userId = claims.get("userId", Long.class);
                stompHeaderAccessor.setUser(new ChatPrincipal(userId));
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new CustomException(ErrorCode.UNAUTHORIZED_TOKEN, "StompChannelInterceptor.preSend");
            }

        }

        return ChannelInterceptor.super.preSend(message, channel);
    }

    private String parseToken(String authorization) {
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        return null;
    }
}
