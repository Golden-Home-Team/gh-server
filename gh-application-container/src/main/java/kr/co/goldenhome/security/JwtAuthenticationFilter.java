package kr.co.goldenhome.security;

import auth.UserPrincipal;
import exception.CustomException;
import exception.ErrorCode;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
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
            throw new CustomException(ErrorCode.UNAUTHORIZED_TOKEN, "JwtAuthenticationFilter.doFilterInternal");
        }
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
}
