package exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {


    INVALID_ENUM(400, "잘못된 enum"),
    INVALID_FILENAME(400, "파일 이름은 한글, 영어, 숫자, 점, 밑줄, 하이픈만 포함할 수 있습니다."),
    INVALID_VERIFICATION_CODE(400, "유효한 인증번호가 존재하지 않거나 만료되었습니다."),
    LOGIN_FAILED(401, "이메일 혹은 비밀번호가 틀립니다."),
    UNAUTHORIZED_TOKEN(401, "유효하지 않은 토큰입니다."),
    SOCIAL_LOGIN_FAILED(401, "소셜 로그인에 실패했습니다."),
    FORBIDDEN(403, "권한이 없습니다."),
    NOT_FOUND(404, "존재하지 않는 리소스입니다."),
    RESUME_NOT_FOUND(404, "이력서 제출전에 작성해야합니다."),
    EMAIL_NOT_FOUND(404, "해당 이메일로 가입한 계정이 존재하지 않습니다."),
    FACILITY_NOT_FOUND(404, "존재하지 않는 시설입니다"),
    DUPLICATED_LOGIN_ID(409, "이미 존재하는 로그인 아이디입니다.");
    private final int httpStatus;
    private final String message;
}
