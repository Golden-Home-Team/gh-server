package kr.co.goldenhome.service;

import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import kr.co.goldenhome.entity.CommunityUser;
import kr.co.goldenhome.entity.InvitationCode;
import kr.co.goldenhome.enums.CommunityUserRole;
import kr.co.goldenhome.enums.InvitationCodeStatus;
import kr.co.goldenhome.repository.CommunityUserRepository;
import kr.co.goldenhome.repository.InvitationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommunityEntryService {

    private final CommunityUserRepository communityUserRepository;
    private final InvitationCodeRepository invitationCodeRepository;

    public String generateInvitationCode(Long facilityId, Long userId) {
        try {
            String code = generateRandomCode();
            invitationCodeRepository.save(InvitationCode.create(code, facilityId, userId));
            return code;
        } catch (DataIntegrityViolationException e) {
            return generateInvitationCode(facilityId, userId);
        }
    }

    @Transactional
    public void enter(String code, Long userId) {
        InvitationCode invitationCode = invitationCodeRepository.findByCodeAndStatus(code, InvitationCodeStatus.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_VERIFICATION_CODE, "CommunityService.enter()"));
        invitationCode.use(userId);
        communityUserRepository.save(CommunityUser.create(invitationCode.getFacilityId(), userId, CommunityUserRole.USER));
    }

    private String generateRandomCode() {
        return UUID.randomUUID().toString().substring(0, 8);
    }


}
