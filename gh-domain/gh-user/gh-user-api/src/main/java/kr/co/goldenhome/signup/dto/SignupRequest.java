package kr.co.goldenhome.signup.dto;

import exception.CustomException;
import jakarta.validation.constraints.NotBlank;
import kr.co.goldenhome.dto.Signup;
import validator.EmailValidator;
import validator.PasswordValidator;
import validator.Validatable;

import java.util.Objects;

import static exception.ErrorCode.*;

public record SignupRequest(@NotBlank String username, @NotBlank String email,
                            @NotBlank String password, @NotBlank String confirmPassword) implements Validatable {

    @Override
    public void validate() {
        EmailValidator.validate(email);
        PasswordValidator.validate(password);
        if(!Objects.requireNonNull(password).equals(confirmPassword)) {
            throw new CustomException(PASSWORD_MISMATCH, "SignupRequest.validate");
        }
    }

    public Signup toSignup() {
        return new Signup(username, email, password);
    }

}
