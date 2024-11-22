package io.woogisfree.eventdrivenordersystem.member.mapper;

import io.woogisfree.eventdrivenordersystem.member.domain.Member;
import io.woogisfree.eventdrivenordersystem.member.dto.MemberResponse;
import io.woogisfree.eventdrivenordersystem.order.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MemberMapper {

    private final OrderMapper orderMapper;

    public MemberResponse toDto(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .address(member.getAddress())
                .orders(member.getOrders().stream()
                        .map(orderMapper::toOrderResponse)
                        .toList())
                .build();
    }


}
