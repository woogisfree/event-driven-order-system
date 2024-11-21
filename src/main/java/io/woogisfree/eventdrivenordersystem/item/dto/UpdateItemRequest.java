package io.woogisfree.eventdrivenordersystem.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateItemRequest {
    private String name;
    private int price;
    private int stockQuantity;
}
