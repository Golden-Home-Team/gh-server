package kr.co.goldenhome.signup.dto;

import jakarta.validation.constraints.NotBlank;
import kr.co.goldenhome.enums.VerificationType;
import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import org.springframework.util.StringUtils;


public record SignupRequest(@NotBlank String loginId, String email,
                            @NotBlank String password, String phoneNumber,
                            @NotBlank String type, @NotBlank String verificationCode)  {

    public String contact() {
        VerificationType verificationType = VerificationType.valueOf(this.type);
        return switch (verificationType) {
            case EMAIL -> {
                if (!StringUtils.hasText(email)) throw new CustomException(ErrorCode.NOT_NULL, "SignupRequest");
                yield this.email;
            }
            case PHONE -> {
                if (!StringUtils.hasText(this.phoneNumber)) throw new CustomException(ErrorCode.NOT_NULL, "SignupRequest.phoneNumber");
                yield this.phoneNumber;
            }
        };
    }
}
