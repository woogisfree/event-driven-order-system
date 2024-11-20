package io.woogisfree.eventdrivenordersystem.order.repository;

import io.woogisfree.eventdrivenordersystem.order.domain.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {"orderItems", "member"})
    List<Order> findAllByMemberId(Long memberId);

    @NonNull
    @Override
    @EntityGraph(attributePaths = {"orderItems", "member"})
    Optional<Order> findById(@NonNull Long orderId);
}
