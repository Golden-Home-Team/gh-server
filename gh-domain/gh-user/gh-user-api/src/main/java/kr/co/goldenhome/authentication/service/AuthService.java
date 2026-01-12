package kr.co.goldenhome.authentication.service;

import kr.co.goldenhome.authentication.implement.VerificationManagerFactory;
import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import kr.co.goldenhome.authentication.dto.*;
import kr.co.goldenhome.entity.User;
import kr.co.goldenhome.infrastructure.PasswordProcessor;
import kr.co.goldenhome.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final VerificationManagerFactory verificationManagerFactory;
    private final UserRepository userRepository;
    private final PasswordProcessor passwordProcessor;

    public String requestVerification(VerificationRequest request) {
        return verificationManagerFactory.requestVerification(request.contact(), request.type());
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        VerificationConfirmServiceResponse response = verificationManagerFactory.confirm(request.type(), request.contact(), request.verificationCode());
        User user = userRepository.findByLoginId(response.loginId()).orElseThrow(() -> new CustomException(ErrorCode.LOGIN_ID_NOT_FOUND, "AuthRecoveryService.resetPassword"));
        String encodedPassword = passwordProcessor.encode(request.newPassword());
        user.resetPassword(encodedPassword);
    }

    @Transactional
    public void resetEmail(ResetEmailRequest request) {
        VerificationConfirmServiceResponse response = verificationManagerFactory.confirm(request.type(), request.contact(), request.verificationCode());
        User user = userRepository.findByLoginId(response.loginId()).orElseThrow(() -> new CustomException(ErrorCode.LOGIN_ID_NOT_FOUND, "AuthRecoveryService.resetPassword"));
        user.resetEmail(request.email());
    }

    @Transactional
    public void resetPhoneNumber(ResetPhoneNumberRequest request) {
        VerificationConfirmServiceResponse response = verificationManagerFactory.confirm(request.type(), request.contact(), request.verificationCode());
        User user = userRepository.findByLoginId(response.loginId()).orElseThrow(() -> new CustomException(ErrorCode.LOGIN_ID_NOT_FOUND, "AuthRecoveryService.resetPassword"));
        user.resetPhoneNumber(request.phoneNumber());
    }

    public FindLoginIdResponse findLoginId(FindLoginIdRequest request) {
        VerificationConfirmServiceResponse response = verificationManagerFactory.confirm(request.type(), request.contact(), request.verificationCode());
        User user = userRepository.findByLoginId(response.loginId()).orElseThrow(() -> new CustomException(ErrorCode.LOGIN_ID_NOT_FOUND, "AuthRecoveryService.resetPassword"));
        return new FindLoginIdResponse(user.getCreatedAt(), user.getLoginId());
    }
}
