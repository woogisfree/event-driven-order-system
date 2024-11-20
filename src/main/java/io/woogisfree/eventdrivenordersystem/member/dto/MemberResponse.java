package io.woogisfree.eventdrivenordersystem.member.dto;

import io.woogisfree.eventdrivenordersystem.order.dto.OrderResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {
    private Long id;
    private String name;
    private String address;
    private List<OrderResponse> orders;
}
