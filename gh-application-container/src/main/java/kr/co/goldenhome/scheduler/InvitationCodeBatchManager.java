package kr.co.goldenhome.scheduler;

import kr.co.goldenhome.repository.InvitationCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvitationCodeBatchManager {

    private final InvitationCodeRepository invitationCodeRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void expireOldInvitationCodes() {
        invitationCodeRepository.updateExpiredInvitationCodes();
    }
}
