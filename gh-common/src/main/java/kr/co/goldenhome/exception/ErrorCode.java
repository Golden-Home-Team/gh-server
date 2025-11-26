package kr.co.goldenhome.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {


    INVALID_ENUM(400, "잘못된 enum"),
    INVALID_FILENAME(400, "파일 이름은 한글, 영어, 숫자, 점, 밑줄, 하이픈만 포함할 수 있습니다."),
    INVALID_VERIFICATION_CODE(400, "유효한 인증번호가 존재하지 않거나 만료되었습니다."),
    INVALID_PASSWORD(400, "비밀번호와 비밀번호 확인이 다릅니다."),
    INVALID_LOGIN_ID(400, "로그인 아이디가 다릅니다."),
    INVALID_RESET_PASSWORD_TOKEN(400, "유효하지 않은 비밀번호 재설정 토큰입니다."),
    INVALID_REQUEST(400, "Request 필드값이 유효하지 않습니다."),
    JSON_PROCESSING_EXCEPTION(400, "Json 변환 중 오류 발생"),
    INVALID_MESSAGE(400, "메시지 전송 중 오류 발생"),
    INVALID_CHAT_ROOM(400, "자기 자신과의 채팅방은 만들 수 없습니다."),
    FCM_FAILED(400, "FCM 전송 중 에러 발생"),
    TERMS_IS_MANDATORY(400, "해당 약관은 필수 동의가 필요합니다."),
    LOGIN_FAILED(401, "아이디 혹은 비밀번호가 틀리거나 유효하지 않은 사용자입니다."),
    UNAUTHORIZED_TOKEN(401, "유효하지 않은 토큰입니다."),
    SOCIAL_LOGIN_FAILED(401, "소셜 로그인에 실패했습니다."),
    FORBIDDEN(403, "권한이 없습니다."),
    FORBIDDEN_USER(403, "비활성화된 사용자입니다."),
    UPDATE_FORBIDDEN(403, "해당 리소스를 수정할 권한이 없습니다."),
    READ_FORBIDDEN(403, "해당 리소스를 조회할 권한이 없습니다."),
    NOT_FOUND(404, "존재하지 않는 리소스입니다."),
    RESUME_NOT_FOUND(404, "이력서 제출전에 작성해야합니다."),
    USER_NOT_FOUND(404, "존재하지 않는 유저"),
    EMAIL_NOT_FOUND(404, "해당 이메일로 가입한 계정이 존재하지 않습니다."),
    COMMUNITY_NOTICE_NOT_FOUND(404, "해당 커뮤니티 공지가 존재하지 않습니다."),
    LOGIN_ID_NOT_FOUND(404, "아이디가 존재하지 않습니다."),
    FACILITY_NOT_FOUND(404, "존재하지 않는 시설입니다"),
    DAILY_DIET_NOT_FOUND(404, "존재하지 않는 오늘의 식단입니다"),
    DAILY_SHOT_NOT_FOUND(404, "존재하지 않는 오늘의 한 컷입니다"),
    DAILY_MEDICATION_NOT_FOUND(404, "존재하지 않는 오늘의 복약입니다"),
    DAILY_REHAB_NOT_FOUND(404, "존재하지 않는 오늘의 재활입니다"),
    SCHEDULE_NOT_FOUND(404, "해당 일정이 존재하지 않습니다."),
    INQUIRY_NOT_FOUND(404, "해당 요청사항이 존재하지 않습니다."),
    OPTION_NOT_FOUND(404, "해당 질문의 옵션이 존재하지 않습니다."),
    SCORE_NOT_FOUND(404, "원점수 합에 해당하는 환산점수 합이 존재하지 않습니다."),
    CHATROOM_PARTICIPANT_NOT_FOUND(404, "해당 참여자는 채팅방 목록에 존재하지 않습니다."),
    TERMS_NOT_FOUND(404, "존재하지 않는 약관입니다."),
    DUPLICATED_LOGIN_ID(409, "이미 존재하는 아이디입니다."),
    DUPLICATED_EMAIL_ID(409, "이미 존재하는 이메일입니다."),
    UNKNOWN_ERROR(500, "알 수 없는 에러");
    private final int httpStatus;
    private final String message;
}
