package io.woogisfree.eventdrivenordersystem.order.dto;

import io.woogisfree.eventdrivenordersystem.order.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long orderId;
    private Long memberId;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private List<OrderItemResponse> orderItems;

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemResponse {
        private Long itemId;
        private String itemName;
        private int orderPrice;
        private int count;
        private int totalPrice;
    }
}
