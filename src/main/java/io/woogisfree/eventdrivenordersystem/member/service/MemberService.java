package io.woogisfree.eventdrivenordersystem.member.service;

import io.woogisfree.eventdrivenordersystem.member.domain.Member;

import java.util.List;

public interface MemberService {
    List<Member> findAllMembers();

    Long createMember(String name, String address);

}
