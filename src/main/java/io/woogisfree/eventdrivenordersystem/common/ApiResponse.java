package io.woogisfree.eventdrivenordersystem.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private ApiStatus status;
    private HttpStatus statusCode;
    private String message;
    private T data;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    // 성공 응답
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .status(ApiStatus.SUCCESS)
                .statusCode(HttpStatus.OK)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> noContent(T data, String message) {
        return ApiResponse.<T>builder()
                .status(ApiStatus.NO_CONTENT)
                .statusCode(HttpStatus.NO_CONTENT)
                .message(message)
                .data(data)
                .build();
    }

    // 오류 응답
    public static <T> ApiResponse<T> error(String message, HttpStatus statusCode) {
        return ApiResponse.<T>builder()
                .status(ApiStatus.ERROR)
                .statusCode(statusCode)
                .message(message)
                .build();
    }

    // 오류 응답 (유효성 검사 실패)
    public static <T> ApiResponse<T> validationError(String message) {
        return ApiResponse.<T>builder()
                .status(ApiStatus.VALIDATION_ERROR)
                .statusCode(HttpStatus.BAD_REQUEST)
                .message(message)
                .build();
    }

    // 리소스를 찾을 수 없음
    public static <T> ApiResponse<T> notFound(String message) {
        return ApiResponse.<T>builder()
                .status(ApiStatus.NOT_FOUND)
                .statusCode(HttpStatus.NOT_FOUND)
                .message(message)
                .build();
    }

    // 처리 중 상태 응답
    public static <T> ApiResponse<T> pending(String message) {
        return ApiResponse.<T>builder()
                .status(ApiStatus.PENDING)
                .statusCode(HttpStatus.ACCEPTED)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> orderStateError(String message) {
        return ApiResponse.<T>builder()
                .status(ApiStatus.ORDER_STATE_ERROR)
                .statusCode(HttpStatus.CONFLICT)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> notEnoughStockError(String message) {
        return ApiResponse.<T>builder()
                .status(ApiStatus.NOT_ENOUGH_STOCK)
                .statusCode(HttpStatus.CONFLICT)
                .message(message)
                .build();
    }
}
