package kr.co.goldenhome;

import kr.co.goldenhome.auth.UserPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithFakeUser> {

    @Override
    public SecurityContext createSecurityContext(WithFakeUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserPrincipal principal = new UserPrincipal(annotation.userId());
        Authentication auth =
                new UsernamePasswordAuthenticationToken(principal, null, List.of());
        context.setAuthentication(auth);
        return context;
    }
}
