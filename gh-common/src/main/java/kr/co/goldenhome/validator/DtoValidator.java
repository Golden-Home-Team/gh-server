package kr.co.goldenhome.validator;

import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DtoValidator {

    public static void password(String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH, "ResetPasswordRequest");
        }
    }

}
