package kr.co.goldenhome.authentication.service;

import exception.CustomException;
import exception.ErrorCode;
import kr.co.goldenhome.authentication.dto.VerificationConfirmRequest;
import kr.co.goldenhome.authentication.dto.VerificationConfirmResponse;
import kr.co.goldenhome.authentication.dto.VerificationRequest;
import kr.co.goldenhome.authentication.dto.VerificationResponse;
import kr.co.goldenhome.enums.VerificationPurpose;
import kr.co.goldenhome.enums.VerificationType;
import kr.co.goldenhome.authentication.implement.VerificationManager;
import kr.co.goldenhome.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthRecoveryService {

    private final List<VerificationManager> verificationManagers;
    private final UserRepository userRepository;

    public VerificationResponse requestVerification(VerificationRequest request) {
        VerificationPurpose purpose = VerificationPurpose.FIND_ID;
        if (StringUtils.hasText(request.loginId())) {
            boolean userExists = userRepository.existsByLoginId(request.loginId());
            if (!userExists) throw new CustomException(ErrorCode.LOGIN_ID_NOT_FOUND, "AuthRecoveryService.requestVerification");
            purpose = VerificationPurpose.RESET_PASSWORD;
        }
        for (VerificationManager verificationManager : verificationManagers) {
            if (verificationManager.getVerificationType() == VerificationType.valueOf(request.type())) {
                String verificationCode = verificationManager.create(request.contact());
                verificationManager.send(request.contact(), verificationCode);
                return new VerificationResponse(verificationCode, purpose);
            }
        }
        return null;
    }

    public VerificationConfirmResponse confirm(VerificationConfirmRequest request) {
        for (VerificationManager verificationManager : verificationManagers) {
            if (verificationManager.getVerificationType() == VerificationType.valueOf(request.type())) {
                return verificationManager.confirm(request.contact(), request.verificationCode(), VerificationPurpose.valueOf(request.purpose()));
            }
        }
        return null;
    }
}
