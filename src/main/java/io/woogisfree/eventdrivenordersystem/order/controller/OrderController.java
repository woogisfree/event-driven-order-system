package io.woogisfree.eventdrivenordersystem.order.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.woogisfree.eventdrivenordersystem.common.ApiResponse;
import io.woogisfree.eventdrivenordersystem.order.dto.CreateOrderRequest;
import io.woogisfree.eventdrivenordersystem.order.dto.OrderResponse;
import io.woogisfree.eventdrivenordersystem.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Order", description = "주문 관리 API")
@RequestMapping("/api/orders")
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        Long orderId = orderService.createOrder(request.getMemberId(), request.getOrderItems());
        return new ResponseEntity<>(ApiResponse.success(orderId, "Order created successfully"), HttpStatus.CREATED);
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return new ResponseEntity<>(ApiResponse.noContent(null, "Order canceled successfully"), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> findOrder(@PathVariable("orderId") Long orderId) {
        OrderResponse response = orderService.findOrder(orderId);
        return new ResponseEntity<>(ApiResponse.success(response, "Order found successfully"), HttpStatus.OK);
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> findOrders(@PathVariable("memberId") Long memberId) {
        List<OrderResponse> response = orderService.findOrdersByMemberId(memberId);
        return new ResponseEntity<>(ApiResponse.success(response, "Orders found successfully"), HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable("orderId") Long orderId) {
        orderService.deleteOrder(orderId);
        return new ResponseEntity<>(ApiResponse.noContent(null, "Order deleted successfully"), HttpStatus.NO_CONTENT);
    }
}
