package kr.co.goldenhome.signup.implement;

import exception.CustomException;
import exception.ErrorCode;
import kr.co.goldenhome.dto.Email;
import kr.co.goldenhome.entity.EmailVerification;
import kr.co.goldenhome.enums.EmailVerificationType;
import kr.co.goldenhome.infrastructure.EmailVerificationRepository;
import kr.co.goldenhome.infrastructure.MailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailVerificationManager {

    private final EmailVerificationRepository emailVerificationRepository;
    private final MailSender mailSender;

    public EmailVerification create(Long userId, String emailAddress, EmailVerificationType type) {
        return emailVerificationRepository.save(EmailVerification.create(userId, emailAddress, type));
    }

    public void sendEmail(EmailVerification emailVerification) {
        mailSender.send(Email.create(emailVerification));
    }

    public Long verify(String code) {
        EmailVerification emailVerification = emailVerificationRepository.findByVerificationCode(code).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "EmailVerificationManager.verify"));
        emailVerification.verify();
        return emailVerification.getUserId();
    }
}
