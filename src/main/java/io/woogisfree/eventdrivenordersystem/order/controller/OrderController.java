package io.woogisfree.eventdrivenordersystem.order.controller;

import io.woogisfree.eventdrivenordersystem.order.domain.Order;
import io.woogisfree.eventdrivenordersystem.order.dto.CreateOrderRequest;
import io.woogisfree.eventdrivenordersystem.order.dto.OrderResponse;
import io.woogisfree.eventdrivenordersystem.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/orders")
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Long> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        Long orderId = orderService.createOrder(request.getMemberId(), request.getItemId(), request.getCount());
        return new ResponseEntity<>(orderId, HttpStatus.CREATED);
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> findOrder(@PathVariable Long orderId) {
        Order order = orderService.findOrder(orderId);
        OrderResponse response = convertToOrderResponse(order);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<List<OrderResponse>> findOrders(@PathVariable Long memberId) {
        List<Order> orders = orderService.findOrders(memberId);
        List<OrderResponse> response = orders.stream()
                .map(this::convertToOrderResponse)
                .toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private OrderResponse convertToOrderResponse(Order order) {
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
}
