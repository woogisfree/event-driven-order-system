package io.woogisfree.eventdrivenordersystem.common;

public enum ApiStatus {
    SUCCESS,          // 성공
    NO_CONTENT,       // 성공하지만 응답 본문이 없음
    ERROR,            // 일반적인 에러
    VALIDATION_ERROR, // 유효성 검사 실패
    NOT_FOUND,        // 리소스를 찾을 수 없음
    PENDING,          // 처리 중

    ORDER_STATE_ERROR,// 주문 상태 에러
    NOT_ENOUGH_STOCK  // 재고 부족 상태
}
