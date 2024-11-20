package io.woogisfree.eventdrivenordersystem.member.service;

import io.woogisfree.eventdrivenordersystem.member.domain.Member;

public interface MemberService {
    Long createMember(String name, String address);

    Member findMemberWithOrders(Long memberId);

    void updateMember(Long memberId, String name, String address);

    void deleteMember(Long memberId);
}
