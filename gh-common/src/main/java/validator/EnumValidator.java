package validator;

import exception.CustomException;
import exception.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnumValidator {

    public static void validate(Class<? extends Enum<?>> enumClass, String targetFieldName, String targetFieldValue, String origin) {
        Enum<?>[] enumConstants = enumClass.getEnumConstants();
        boolean isValid = Arrays.stream(enumConstants)
                .map(Enum::name)
                .anyMatch(name -> name.equalsIgnoreCase(targetFieldValue));

        if (!isValid) {
            log.error("Target field [{}] 는 [{}] 일 수 없습니다.", targetFieldName, targetFieldValue);
            throw new CustomException(ErrorCode.INVALID_ENUM, origin);
        }
    }
}
