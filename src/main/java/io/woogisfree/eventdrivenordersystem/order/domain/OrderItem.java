package io.woogisfree.eventdrivenordersystem.order.domain;

import io.woogisfree.eventdrivenordersystem.item.domain.Item;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Table(name = "order_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int count;

    public static OrderItem createOrderItem(Item item, int count, Order order) {
        item.reduceStock(count);
        OrderItem orderItem = new OrderItem();
        orderItem.item = item;
        orderItem.orderPrice = item.getPrice();
        orderItem.count = count;
        orderItem.order = order;
        return orderItem;
    }

    public void cancel() {
        item.addStock(count);
    }

    public int getTotalPrice() {
        return orderPrice * count;
    }
}
