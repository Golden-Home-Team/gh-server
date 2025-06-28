package kr.co.goldenhome.signup;

import exception.CustomException;
import org.springframework.http.ResponseEntity;


public class CustomErrorResponse {
    private int status;
    private String message;


    public static ResponseEntity<CustomErrorResponse> toResponse(CustomException e) {
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(new CustomErrorResponse(e.getErrorCode().getHttpStatus(), e.getErrorCode().getMessage()));

    }

    public CustomErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }


    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
