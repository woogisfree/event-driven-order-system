package io.woogisfree.eventdrivenordersystem.order.service;

import io.woogisfree.eventdrivenordersystem.order.dto.OrderResponse;

import java.util.List;

public interface OrderService {
    Long createOrder(Long memberId, Long itemId, int count);

    void cancelOrder(Long orderId);

    OrderResponse findOrder(Long orderId);

    List<OrderResponse> findOrders(Long memberId);

    void deleteOrder(Long orderId);
}
