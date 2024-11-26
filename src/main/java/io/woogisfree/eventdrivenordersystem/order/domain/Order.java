package io.woogisfree.eventdrivenordersystem.order.domain;

import io.woogisfree.eventdrivenordersystem.common.BaseEntity;
import io.woogisfree.eventdrivenordersystem.member.domain.Member;
import io.woogisfree.eventdrivenordersystem.order.exception.OrderStateException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    public static Order createOrder(Member member, OrderItem... orderItems) {
        Order order = new Order();
        order.member = member;
        order.orderDate = LocalDateTime.now();
        order.status = OrderStatus.ORDERED;
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        return order;
    }

    private void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.assignToOrder(this);
    }

    public void cancel() {
        if (status == OrderStatus.DELIVERED) {
            throw new OrderStateException("Cannot cancel delivered order");
        }
        this.status = OrderStatus.CANCELLED;
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }
}
