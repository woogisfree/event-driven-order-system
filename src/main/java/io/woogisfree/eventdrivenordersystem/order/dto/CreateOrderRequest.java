package io.woogisfree.eventdrivenordersystem.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    @NotBlank
    private Long memberId;
    @NotBlank
    private Long itemId;
    @NotBlank
    private int count;
}
