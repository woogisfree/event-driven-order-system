package io.woogisfree.eventdrivenordersystem.member.controller;

import io.woogisfree.eventdrivenordersystem.member.domain.Member;
import io.woogisfree.eventdrivenordersystem.member.dto.CreateMemberRequest;
import io.woogisfree.eventdrivenordersystem.member.dto.MemberResponse;
import io.woogisfree.eventdrivenordersystem.member.dto.UpdateMemberRequest;
import io.woogisfree.eventdrivenordersystem.member.service.MemberService;
import io.woogisfree.eventdrivenordersystem.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/members")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Long> createMember(@Valid @RequestBody CreateMemberRequest request) {
        Long memberId = memberService.createMember(request.getName(), request.getAddress());
        return new ResponseEntity<>(memberId, HttpStatus.CREATED);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> findMember(@PathVariable Long memberId) {
        Member member = memberService.findMemberWithOrders(memberId);
        MemberResponse memberResponse = MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .address(member.getAddress())
                .orders(member.getOrders().stream()
                        .map(orderService::convertToOrderResponse)
                        .toList())
                .build();
        return new ResponseEntity<>(memberResponse, HttpStatus.OK);
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<Void> updateMember(@PathVariable Long memberId, @Valid @RequestBody UpdateMemberRequest request) {
        memberService.updateMember(memberId, request.getName(), request.getAddress());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
