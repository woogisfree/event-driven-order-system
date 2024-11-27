package io.woogisfree.eventdrivenordersystem.member.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.woogisfree.eventdrivenordersystem.common.ApiResponse;
import io.woogisfree.eventdrivenordersystem.member.dto.CreateMemberRequest;
import io.woogisfree.eventdrivenordersystem.member.dto.MemberResponse;
import io.woogisfree.eventdrivenordersystem.member.dto.UpdateMemberRequest;
import io.woogisfree.eventdrivenordersystem.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member", description = "회원 관리 API")
@RequestMapping("/api/members")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createMember(@Valid @RequestBody CreateMemberRequest request) {
        Long memberId = memberService.createMember(request.getName(), request.getAddress());
        return new ResponseEntity<>(ApiResponse.success(memberId, "Member created successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<ApiResponse<MemberResponse>> findMember(@PathVariable("memberId") Long memberId) {
        MemberResponse response = memberService.findMemberWithOrders(memberId);
        return new ResponseEntity<>(ApiResponse.success(response, "Member found successfully"), HttpStatus.OK);
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<ApiResponse<Void>> updateMember(@PathVariable("memberId") Long memberId, @Valid @RequestBody UpdateMemberRequest request) {
        memberService.updateMember(memberId, request.getName(), request.getAddress());
        return new ResponseEntity<>(ApiResponse.noContent(null, "Member updated successfully"), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<ApiResponse<Void>> deleteMember(@PathVariable("memberId") Long memberId) {
        memberService.deleteMember(memberId);
        return new ResponseEntity<>(ApiResponse.noContent(null, "Member deleted successfully"), HttpStatus.NO_CONTENT);
    }
}
