package io.woogisfree.eventdrivenordersystem.member.service;

import io.woogisfree.eventdrivenordersystem.member.domain.Member;
import io.woogisfree.eventdrivenordersystem.member.dto.MemberResponse;
import io.woogisfree.eventdrivenordersystem.member.exception.MemberNotFoundException;
import io.woogisfree.eventdrivenordersystem.member.mapper.MemberMapper;
import io.woogisfree.eventdrivenordersystem.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Transactional
    public Long createMember(String name, String address) {
        Member member = Member.builder()
                .name(name)
                .address(address)
                .build();
        memberRepository.save(member);
        return member.getId();
    }

    public MemberResponse findMemberWithOrders(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member with ID " + memberId + " does not exist."));
        return memberMapper.toDto(member);
    }

    @Transactional
    public void updateMember(Long memberId, String name, String address) {
        memberRepository.findById(memberId)
                .ifPresentOrElse(member -> {
                    member.update(name, address);
                }, () -> {
                    throw new MemberNotFoundException("Member with ID " + memberId + " does not exist.");
                });
    }

    @Transactional
    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member with ID " + memberId + " does not exist."));
        memberRepository.delete(member);
    }
}
