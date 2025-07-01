package exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_EMAIL(400, "잘못된 이메일 형식입니다."),
    INVALID_PASSWORD(400, "잘못된 비밀번호 형식입니다."),
    PASSWORD_MISMATCH(400, "비밀번호와 비밀번호 확인이 다릅니다."),
    LOGIN_FAILED(401, "이메일 혹은 비밀번호가 틀립니다."),
    UNAUTHORIZED_TOKEN(401, "유효하지 않은 토큰입니다."),
    NOT_FOUND(404, "존재하지 않는 리소스입니다."),
    DUPLICATED_LOGIN_ID(409, "이미 존재하는 로그인 아이디입니다.");
    private final int httpStatus;
    private final String message;
}
