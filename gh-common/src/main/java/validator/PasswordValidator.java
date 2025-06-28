package validator;

import exception.CustomException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static exception.ErrorCode.INVALID_PASSWORD;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PasswordValidator {
    private static final String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{8,}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_REGEX);

    public static void validate(String password) {
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw new CustomException(INVALID_PASSWORD, "PasswordValidator.validate");
        }

    }
}
