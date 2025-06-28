package kr.co.goldenhome.dto;

import kr.co.goldenhome.entity.EmailVerification;
import kr.co.goldenhome.template.EmailVerificationTemplate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Email {

    private String to;
    private String subject;
    private String content;

    public static Email create(EmailVerification emailVerification) {

        EmailVerificationTemplate template = emailVerification.getEmailVerificationType().create(emailVerification.getVerificationCode());

        return Email.builder()
                .to(emailVerification.getEmailAddress())
                .subject(template.getTitle())
                .content(template.getContent())
                .build();
    }
}
