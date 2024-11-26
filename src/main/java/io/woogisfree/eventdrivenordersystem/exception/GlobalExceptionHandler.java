package io.woogisfree.eventdrivenordersystem.exception;

import io.woogisfree.eventdrivenordersystem.common.ApiResponse;
import io.woogisfree.eventdrivenordersystem.item.exception.ItemNotFoundException;
import io.woogisfree.eventdrivenordersystem.item.exception.NotEnoughStockException;
import io.woogisfree.eventdrivenordersystem.member.exception.MemberNotFoundException;
import io.woogisfree.eventdrivenordersystem.order.exception.OrderNotFoundException;
import io.woogisfree.eventdrivenordersystem.order.exception.OrderStateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleMemberNotFoundException(MemberNotFoundException e) {
        return new ResponseEntity<>(ApiResponse.notFound(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleOrderNotFoundException(OrderNotFoundException e) {
        return new ResponseEntity<>(ApiResponse.notFound(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleItemNotFoundException(ItemNotFoundException e) {
        return new ResponseEntity<>(ApiResponse.notFound(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderStateException.class)
    public ResponseEntity<ApiResponse<String>> handleOrderStateException(OrderStateException e) {
        return new ResponseEntity<>(ApiResponse.orderStateError(e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotEnoughStockException.class)
    public ResponseEntity<ApiResponse<String>> handleNotEnoughStockException(NotEnoughStockException e) {
        return new ResponseEntity<>(ApiResponse.notEnoughStockError(e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGlobalException(Exception e) {
        return new ResponseEntity<>(ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
