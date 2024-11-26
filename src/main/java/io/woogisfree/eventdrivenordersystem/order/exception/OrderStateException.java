package io.woogisfree.eventdrivenordersystem.order.exception;

public class OrderStateException extends RuntimeException {

    public OrderStateException() {
        super();
    }

    public OrderStateException(Throwable cause) {
        super(cause);
    }

    public OrderStateException(String message) {
        super(message);
    }

    public OrderStateException(String message, Throwable cause) {
        super(message, cause);
    }

    protected OrderStateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
