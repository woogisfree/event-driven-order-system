package io.woogisfree.eventdrivenordersystem.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private ApiStatus status;
    private HttpStatus statusCode;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    // 성공 응답
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(ApiStatus.SUCCESS, HttpStatus.OK, message, data, LocalDateTime.now());
    }

    // 오류 응답
    public static <T> ApiResponse<T> error(String message, HttpStatus statusCode) {
        return new ApiResponse<>(ApiStatus.ERROR, statusCode, message, null, LocalDateTime.now());
    }

    // 오류 응답 (유효성 검사 실패)
    public static <T> ApiResponse<T> validationError(String message) {
        return new ApiResponse<>(ApiStatus.VALIDATION_ERROR, HttpStatus.BAD_REQUEST, message, null, LocalDateTime.now());
    }

    // 리소스를 찾을 수 없음
    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(ApiStatus.NOT_FOUND, HttpStatus.NOT_FOUND, message, null, LocalDateTime.now());
    }

    // 처리 중 상태 응답
    public static <T> ApiResponse<T> pending(String message) {
        return new ApiResponse<>(ApiStatus.PENDING, HttpStatus.ACCEPTED, message, null, LocalDateTime.now());
    }
}
