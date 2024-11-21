package io.woogisfree.eventdrivenordersystem.order.controller;

import io.woogisfree.eventdrivenordersystem.common.ApiResponse;
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

@RequestMapping("/api/orders")
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        Long orderId = orderService.createOrder(request.getMemberId(), request.getItemId(), request.getCount());
        return new ResponseEntity<>(ApiResponse.success(orderId, "Order created successfully"), HttpStatus.CREATED);
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return new ResponseEntity<>(ApiResponse.success(null, "Order canceled successfully"), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> findOrder(@PathVariable Long orderId) {
        Order order = orderService.findOrder(orderId);
        OrderResponse response = orderService.convertToOrderResponse(order);
        return new ResponseEntity<>(ApiResponse.success(response, "Order found successfully"), HttpStatus.OK);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> findOrders(@PathVariable Long memberId) {
        List<Order> orders = orderService.findOrders(memberId);
        List<OrderResponse> response = orders.stream()
                .map(orderService::convertToOrderResponse)
                .toList();
        return new ResponseEntity<>(ApiResponse.success(response, "Orders found successfully"), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return new ResponseEntity<>(ApiResponse.success(null, "Order deleted successfully"), HttpStatus.NO_CONTENT);
    }
}
