package io.woogisfree.eventdrivenordersystem.order.mapper;

import io.woogisfree.eventdrivenordersystem.order.domain.Order;
import io.woogisfree.eventdrivenordersystem.order.dto.OrderResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderResponse toOrderResponse(Order order) {
        return OrderResponse.builder()
                .orderId(order.getId())
                .memberId(order.getMember().getId())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .orderItems(order.getOrderItems().stream()
                        .map(orderItem -> OrderResponse.OrderItemResponse.builder()
                                .itemId(orderItem.getItem().getId())
                                .itemName(orderItem.getItem().getName())
                                .orderPrice(orderItem.getOrderPrice())
                                .count(orderItem.getCount())
                                .totalPrice(orderItem.getTotalPrice())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public List<OrderResponse> toOrderResponseList(List<Order> orders) {
        return orders.stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());
    }
}
