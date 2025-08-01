package kr.co.goldenhome.authentication.service;

import exception.CustomException;
import exception.ErrorCode;
import kr.co.goldenhome.authentication.dto.*;
import kr.co.goldenhome.entity.User;
import kr.co.goldenhome.enums.VerificationPurpose;
import kr.co.goldenhome.enums.VerificationType;
import kr.co.goldenhome.authentication.implement.VerificationManager;
import kr.co.goldenhome.infrastructure.PasswordProcessor;
import kr.co.goldenhome.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthRecoveryService {

    private final List<VerificationManager> verificationManagers;
    private final UserRepository userRepository;
    private final PasswordProcessor passwordProcessor;

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

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        if (!Objects.equals(request.newPassword(), request.confirmPassword())) throw new CustomException(ErrorCode.INVALID_PASSWORD, "AuthRecoveryService.resetPassword");
        String encodedPassword = passwordProcessor.encode(request.newPassword());
        User user = userRepository.findByLoginId(request.loginId()).orElseThrow(() -> new CustomException(ErrorCode.LOGIN_ID_NOT_FOUND, "AuthRecoveryService.resetPassword"));
        user.resetPassword(encodedPassword);
    }
}
