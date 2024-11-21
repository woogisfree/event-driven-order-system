package io.woogisfree.eventdrivenordersystem.common;

public enum ApiStatus {
    SUCCESS,          // 성공
    ERROR,            // 일반적인 에러
    VALIDATION_ERROR, // 유효성 검사 실패
    NOT_FOUND,        // 리소스를 찾을 수 없음
    PENDING           // 처리 중
}
