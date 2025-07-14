package kr.co.goldenhome.enums;

public enum AdmissionStatus {
    PENDING_REVIEW,          // 열람 전
    REVIEWED,                // 열람 완료
    IN_PROGRESS,             // 심사 중
    ELIGIBLE_FOR_ADMISSION,  // 입소 가능
    NOT_ELIGIBLE_FOR_ADMISSION, // 입소 불가
    ADMITTED                 // 입소 완료
}
