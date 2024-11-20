package io.woogisfree.eventdrivenordersystem.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    @NotNull
    private Long memberId;
    @NotNull
    private Long itemId;
    @NotNull
    private int count;
}
