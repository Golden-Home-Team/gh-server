package kr.co.goldenhome.enums;

import kr.co.goldenhome.template.EmailVerificationTemplate;
import kr.co.goldenhome.template.SignupVerificationTemplate;

public enum EmailVerificationType {
    SIGN_UP {
        @Override
        public EmailVerificationTemplate create(String verificationCode) {
            return new SignupVerificationTemplate(verificationCode);
        }
    },
    CHANGE_PASSWORD {
        @Override
        public EmailVerificationTemplate create(String verificationCode) {
            return null;
        }
    };

    public abstract EmailVerificationTemplate create(String verificationCode);
}
