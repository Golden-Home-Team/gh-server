package kr.co.goldenhome.authentication.service;

import kr.co.goldenhome.authentication.dto.VerificationConfirmRequest;
import kr.co.goldenhome.authentication.dto.VerificationConfirmResponse;
import kr.co.goldenhome.authentication.dto.VerificationRequest;
import kr.co.goldenhome.authentication.dto.VerificationResponse;
import kr.co.goldenhome.enums.VerificationType;
import kr.co.goldenhome.authentication.implement.VerificationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthRecoveryService {

    private final List<VerificationManager> verificationManagers;

    public VerificationResponse requestVerification(VerificationRequest request) {
        for (VerificationManager verificationManager : verificationManagers) {
            if (verificationManager.getVerificationType() == VerificationType.valueOf(request.type())) {
                String verificationCode = verificationManager.create(request.contact());
                verificationManager.send(request.contact(), verificationCode);
                return new VerificationResponse(verificationCode);
            }
        }
        return null;
    }

    public VerificationConfirmResponse confirm(VerificationConfirmRequest request) {
        for (VerificationManager verificationManager : verificationManagers) {
            if (verificationManager.getVerificationType() == VerificationType.valueOf(request.type())) {
                return verificationManager.confirm(request.contact(), request.verificationCode());
            }
        }
        return null;
    }
}
