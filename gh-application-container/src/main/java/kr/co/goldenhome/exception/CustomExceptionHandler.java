package kr.co.goldenhome.exception;

import exception.CustomException;
import exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("CustomException occurred. message={}, origin={}", e.getMessage(), e.getOrigin());
        return ErrorResponse.toResponse(e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unknown Exception occurred. message={}, className={}", e.getMessage(), e.getClass().getName());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(500, "서버에 문제가 발생했습니다."));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        log.error("Bind Exception occurred. message={}, className={}", e.getMessage(), e.getClass().getName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(400, createMessage(e)));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException e) {
        log.error("NoResourceFoundException occurred. message={}, className={}", e.getMessage(), e.getClass().getName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(400, "존재하지 않는 리소스입니다."));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("MissingServletRequestParameterException occurred. parameterName={}, message={}, className={}", e.getParameterName(),  e.getMessage(), e.getClass().getName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(400 , "잘못 요청파라미터 입니다."));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("MethodArgumentTypeMismatchException occurred. message={}, className={}",   e.getMessage(), e.getClass().getName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(400, "메서드의 파라미터 타입과 일치하지 않습니다."));
    }

    private String createMessage(BindException e) {
        if (e.getFieldError() != null && e.getFieldError().getDefaultMessage() != null) {
            return e.getFieldError().getDefaultMessage();
        }
        return e.getFieldErrors().stream()
                .map(FieldError::getField)
                .collect(Collectors.joining(", ")) + " 값들이 정확하지 않습니다.";
    }



}
