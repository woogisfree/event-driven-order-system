package io.woogisfree.eventdrivenordersystem.member.repository;

import io.woogisfree.eventdrivenordersystem.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
