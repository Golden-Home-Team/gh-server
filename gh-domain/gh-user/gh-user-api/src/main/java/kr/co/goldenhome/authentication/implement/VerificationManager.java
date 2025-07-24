package kr.co.goldenhome.authentication.implement;

import kr.co.goldenhome.authentication.dto.VerificationConfirmResponse;
import kr.co.goldenhome.enums.VerificationType;

public interface VerificationManager {
    String create(String contact);
    VerificationType getVerificationType();
    void send(String contact, String verificationCode);
    VerificationConfirmResponse confirm(String contact, String verificationCode);
}
