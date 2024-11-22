package io.woogisfree.eventdrivenordersystem.member.service;

import io.woogisfree.eventdrivenordersystem.member.dto.MemberResponse;

public interface MemberService {
    Long createMember(String name, String address);

    MemberResponse findMemberWithOrders(Long memberId);

    void updateMember(Long memberId, String name, String address);

    void deleteMember(Long memberId);
}
