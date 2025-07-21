package validator;

import exception.CustomException;
import exception.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileValidator {

    /**
     * 한글, 영어, 숫자, 점, 밑줄, 하이픈 허용
     */
    private static final String ALLOWED_S3_KEY_CHARS_REGEX = "^[가-힣a-zA-Z0-9._-]+$";

    public static void validateFileName(String fileName) {
        Pattern pattern = Pattern.compile(ALLOWED_S3_KEY_CHARS_REGEX);
        Matcher matcher = pattern.matcher(fileName);
        if (!matcher.matches()) throw new CustomException(ErrorCode.INVALID_FILENAME, "FileValidator.validateFileName");
    }

}
