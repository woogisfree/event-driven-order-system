package io.woogisfree.eventdrivenordersystem.order.service;

import io.woogisfree.eventdrivenordersystem.order.domain.Order;

import java.util.List;

public interface OrderService {
    Long createOrder(Long memberId, Long itemId, int count);

    void cancelOrder(Long orderId);

    Order findOrder(Long orderId);

    List<Order> findOrders(Long memberId);

    void deleteOrder(Long orderId);
}
