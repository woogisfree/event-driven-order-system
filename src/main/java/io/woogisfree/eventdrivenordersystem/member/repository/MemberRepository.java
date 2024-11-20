package io.woogisfree.eventdrivenordersystem.member.repository;

import io.woogisfree.eventdrivenordersystem.member.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @NonNull
    @Override
    @EntityGraph(attributePaths = {"orders", "orders.orderItems"})
    Optional<Member> findById(@NonNull Long memberId);
}
