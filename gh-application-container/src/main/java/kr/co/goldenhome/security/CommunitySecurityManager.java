package kr.co.goldenhome.security;

import auth.UserPrincipal;
import kr.co.goldenhome.repository.CommunityUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommunitySecurityManager {

    private final CommunityUserRepository communityUserRepository;

    public boolean isMember(Long facilityId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return false;
        }
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        Long userId = principal.userId();
        return communityUserRepository.findByFacilityIdAndUserId(facilityId, userId).isPresent();
    }
}
