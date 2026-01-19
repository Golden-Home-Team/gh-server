package kr.co.goldenhome.authentication.implement;

import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import kr.co.goldenhome.entity.EmailVerification;
import kr.co.goldenhome.enums.VerificationType;
import kr.co.goldenhome.infrastructure.EmailVerificationRepository;
import kr.co.goldenhome.infrastructure.MailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class EmailVerificationManager implements VerificationManager {

    private final EmailVerificationRepository emailVerificationRepository;
    private final MailSender mailSender;

    @Override
    public String create(String contact) {
        EmailVerification emailVerification = emailVerificationRepository.save(EmailVerification.create(contact));
        return emailVerification.getVerificationCode();
    }

    @Override
    public VerificationType getVerificationType() {
        return VerificationType.EMAIL;
    }

    @Override
    public void send(String contact, String verificationCode) {
        mailSender.send(contact, "골든홈 인증 메일입니다.", verificationCode);
    }

    @Override
    @Transactional
    public void confirm(String emailAddress, String verificationCode) {
        EmailVerification emailVerification = emailVerificationRepository.findTopByEmailAddressAndUsedIsFalseAndExpiresAtAfterOrderByCreatedAtDesc(emailAddress, LocalDateTime.now()).orElseThrow(() -> new CustomException(ErrorCode.INVALID_VERIFICATION_CODE, "EmailVerificationManager.confirm"));
        if (!emailVerification.getVerificationCode().equals(verificationCode)) throw new CustomException(ErrorCode.INVALID_VERIFICATION_CODE, "EmailVerificationManager.confirm");
        emailVerification.markAsUsed();
    }


}
