package exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_EMAIL(400, "잘못된 이메일 형식입니다."),
    INVALID_PASSWORD(400, "잘못된 비밀번호 형식입니다."),
    PASSWORD_MISMATCH(400, "비밀번호와 비밀번호 확인이 다릅니다."),
    NO_RESOURCE(400, "존재하지 않는 리소스입니다.");
    private final int httpStatus;
    private final String message;
}
