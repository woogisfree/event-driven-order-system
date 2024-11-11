package io.woogisfree.eventdrivenordersystem.order.domain;

/**
 * TODO : 필요하다면 상태를 추가할 수도 있다.
 * 1.	ORDERED: 주문이 완료된 상태.
 * 2.	PAID: 결제가 완료된 상태.
 * 3.	PROCESSING: 주문을 준비 중인 상태(예: 재고 확보, 주문 확인 등).
 * 4.	SHIPPED: 배송이 시작된 상태.
 * 5.	DELIVERED: 배송이 완료되어 고객이 수령한 상태.
 * 6.	CANCELLED: 주문이 취소된 상태.
 * 7.	RETURN_REQUESTED: 고객이 반품을 요청한 상태.
 * 8.	RETURNED: 반품이 완료된 상태.
 * 9.	REFUNDED: 환불이 완료된 상태.
 */
public enum OrderStatus {
    ORDERED, SHIPPED, DELIVERED, CANCELLED
}

