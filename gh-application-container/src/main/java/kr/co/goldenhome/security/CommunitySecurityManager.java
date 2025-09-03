package kr.co.goldenhome.security;

import kr.co.goldenhome.auth.UserPrincipal;
import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import kr.co.goldenhome.entity.CommunityUser;
import kr.co.goldenhome.enums.CommunityUserRole;
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

    public boolean isManager(Long facilityId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return false;
        }
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        Long userId = principal.userId();
        CommunityUser communityUser = communityUserRepository.findByFacilityIdAndUserId(facilityId, userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, "CommunitySecurityManager.isCommunityAdmin"));
        return communityUser.getRole() == CommunityUserRole.MANAGER;
    }
}
