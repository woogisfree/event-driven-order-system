package io.woogisfree.eventdrivenordersystem.order.repository;

import io.woogisfree.eventdrivenordersystem.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o join fetch o.member m where o.member.id = :memberId")
    List<Order> findAllByMemberId(@Param("memberId") Long memberId);

    @Query("select o from Order o join fetch o.orderItems where o.id = :orderId")
    Optional<Order> findOrderWithOrderItemsByOrderId(@Param("orderId") Long orderId);
}
