package kr.co.goldenhome.authentication.service;

import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import kr.co.goldenhome.authentication.dto.*;
import kr.co.goldenhome.entity.User;
import kr.co.goldenhome.enums.VerificationType;
import kr.co.goldenhome.authentication.implement.VerificationManager;
import kr.co.goldenhome.infrastructure.PasswordProcessor;
import kr.co.goldenhome.infrastructure.TokenRepository;
import kr.co.goldenhome.infrastructure.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class AuthRecoveryService {

    private final List<VerificationManager> verificationManagers;
    private final UserRepository userRepository;
    private final PasswordProcessor passwordProcessor;
    private final TokenRepository resetPasswordTokenRepository;

    public AuthRecoveryService(List<VerificationManager> verificationManagers,
                               UserRepository userRepository, PasswordProcessor passwordProcessor,
                               @Qualifier("resetPasswordTokenRepository") TokenRepository resetPasswordTokenRepository) {
        this.verificationManagers = verificationManagers;
        this.userRepository = userRepository;
        this.passwordProcessor = passwordProcessor;
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
    }

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

        VerificationConfirmServiceResponse serviceResponse = null;

        for (VerificationManager verificationManager : verificationManagers) {
            if (verificationManager.getVerificationType() == VerificationType.valueOf(request.type())) {
                serviceResponse = verificationManager.confirm(request.contact(), request.verificationCode());
                break;
            }
        }

        assert serviceResponse != null;

        if (StringUtils.hasText(request.loginId())) {
            if (!request.loginId().equals(serviceResponse.loginId())) throw new CustomException(ErrorCode.INVALID_LOGIN_ID, "AuthRecoveryService.confirm");
            String resetPasswordKey = UUID.randomUUID().toString();
            resetPasswordTokenRepository.save(resetPasswordKey, request.loginId(), Duration.ofMinutes(5));
            return VerificationConfirmResponse.of(serviceResponse, resetPasswordKey);
        }

        return VerificationConfirmResponse.from(serviceResponse);

    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        validateResetPasswordRequest(request);
        String encodedPassword = passwordProcessor.encode(request.newPassword());
        User user = userRepository.findByLoginId(request.loginId()).orElseThrow(() -> new CustomException(ErrorCode.LOGIN_ID_NOT_FOUND, "AuthRecoveryService.resetPassword"));
        user.resetPassword(encodedPassword);
    }

    private void validateResetPasswordRequest(ResetPasswordRequest request) {
        if (!Objects.equals(request.newPassword(), request.confirmPassword())) throw new CustomException(ErrorCode.INVALID_PASSWORD, "AuthRecoveryService.resetPassword");
        String loginIdFromToken = resetPasswordTokenRepository.getByKey(request.resetPasswordToken());
        if (!StringUtils.hasText(loginIdFromToken) || !Objects.equals(request.loginId(), loginIdFromToken)) throw new CustomException(ErrorCode.INVALID_RESET_PASSWORD_TOKEN, "AuthRecoveryService.resetPassword");
    }

}
