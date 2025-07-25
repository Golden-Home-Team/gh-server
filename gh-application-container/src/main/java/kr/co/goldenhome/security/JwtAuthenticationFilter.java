package kr.co.goldenhome.security;

import auth.UserPrincipal;
import exception.CustomException;
import exception.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.goldenhome.authentication.implement.AuthenticationTokenManager;
import kr.co.goldenhome.entity.User;
import kr.co.goldenhome.enums.UserRole;
import kr.co.goldenhome.infrastructure.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationTokenManager authenticationTokenManager;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String accessToken = parseToken(request.getHeader("Authorization"));
        if (!StringUtils.hasText(accessToken)) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "토큰이 제공되지 않았습니다.");
            return;
        }
        try {
            Long userId = authenticationTokenManager.getUserId(accessToken);
            User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "JwtAuthenticationFilter.doFilterInternal"));
            UserPrincipal userPrincipal = new UserPrincipal(userId);
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                    userPrincipal,
                    user.getLoginId(),
                    getAuthorities(user)
            );
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다.");
        } catch (MalformedJwtException e) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
        } catch (CustomException e) {
            sendErrorResponse(response, HttpStatus.valueOf(e.getErrorCode().getHttpStatus()), e.getErrorCode().getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            sendErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "인증 중 알 수 없는 오류가 발생했습니다.");
        }

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {"/api/auth", "/api/users/signup", "/api/facility"};
        String path = request.getRequestURI();
        return Arrays.stream(excludePath).anyMatch(path::startsWith) ||
                path.endsWith(".html");
    }


    private String parseToken(String authorization) {
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        return null;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getUserRole() == UserRole.USER) {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return authorities;
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(String.format("{\"timestamp\": \"%s\", \"status\": %d, \"error\": \"%s\", \"message\": \"%s\"}",
                java.time.OffsetDateTime.now(), status.value(), status.getReasonPhrase(), message));
        response.getWriter().flush();
    }
}
