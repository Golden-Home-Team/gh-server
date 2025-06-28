package kr.co.goldenhome.exception;

import exception.CustomException;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ErrorResponse {
    private int status;
    private String message;

    public static ResponseEntity<ErrorResponse> toResponse(CustomException e) {
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(ErrorResponse.builder()
                        .status(e.getErrorCode().getHttpStatus())
                        .message(e.getErrorCode().getMessage())
                        .build());
    }
}
