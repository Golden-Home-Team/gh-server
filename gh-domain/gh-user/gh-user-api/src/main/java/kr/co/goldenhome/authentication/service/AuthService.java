package kr.co.goldenhome.authentication.service;

import kr.co.goldenhome.authentication.implement.VerificationManagerFactory;
import kr.co.goldenhome.entity.QUser;
import kr.co.goldenhome.enums.VerificationType;
import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import kr.co.goldenhome.authentication.dto.*;
import kr.co.goldenhome.entity.User;
import kr.co.goldenhome.infrastructure.PasswordProcessor;
import kr.co.goldenhome.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


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
    public void resetPassword(ResetPasswordRequest request, Long userId) {
        verificationManagerFactory.confirm(request.type(), request.contact(), request.verificationCode());
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.LOGIN_ID_NOT_FOUND, "AuthRecoveryService.resetPassword"));
        String encodedPassword = passwordProcessor.encode(request.newPassword());
        user.resetPassword(encodedPassword);
    }

    @Transactional
    public void resetEmail(ResetEmailRequest request, Long userId) {
        verificationManagerFactory.confirm(request.type(), request.contact(), request.verificationCode());
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.LOGIN_ID_NOT_FOUND, "AuthRecoveryService.resetEmail"));
        user.resetEmail(request.email());
    }

    @Transactional
    public void resetPhoneNumber(ResetPhoneNumberRequest request, Long userId) {
        verificationManagerFactory.confirm(request.type(), request.contact(), request.verificationCode());
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.LOGIN_ID_NOT_FOUND, "AuthRecoveryService.resetPhoneNumber"));
        user.resetPhoneNumber(request.phoneNumber());
    }

    public FindLoginIdResponse findLoginId(FindLoginIdRequest request) {
        verificationManagerFactory.confirm(request.type(), request.contact(), request.verificationCode());
        VerificationType verificationType = VerificationType.valueOf(request.type());
        User user = findUserByContact(verificationType, request.contact()).orElseThrow(() -> new CustomException(ErrorCode.LOGIN_ID_NOT_FOUND, "AuthRecoveryService.findLoginId"));
        return new FindLoginIdResponse(user.getCreatedAt(), user.getLoginId());
    }

    private Optional<User> findUserByContact(VerificationType type, String contact) {
        return switch (type) {
            case EMAIL -> userRepository.findByEmail(contact);
            case PHONE -> userRepository.findByPhoneNumber(contact);
        };
    }
}
