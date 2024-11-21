package io.woogisfree.eventdrivenordersystem.order.repository;

import io.woogisfree.eventdrivenordersystem.order.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
