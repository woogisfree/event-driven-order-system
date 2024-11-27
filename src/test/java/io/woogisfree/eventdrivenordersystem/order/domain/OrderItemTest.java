package io.woogisfree.eventdrivenordersystem.order.domain;

import io.woogisfree.eventdrivenordersystem.item.domain.Item;
import io.woogisfree.eventdrivenordersystem.item.exception.NotEnoughStockException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderItemTest {

    @DisplayName("재고가 충분할 때 주문 아이템 생성하면 재고가 줄어든다.")
    @Test
    void createOrderItemWithEnoughStock() {
        //given
        Item item = Item.builder()
                .name("test item")
                .price(1000)
                .stockQuantity(10)
                .build();
        int orderCount = 9;

        //when
        OrderItem.createOrderItem(item, item.getPrice(), orderCount);

        //then
        assertThat(item.getStockQuantity()).isEqualTo(1);
    }

    @DisplayName("재고가 부족할 때 주문 아이템 생성 시 NotEnoughStockException 예외가 발생한다.")
    @Test
    void createOrderItemWithNotEnoughStock() {
        //given
        Item item = Item.builder()
                .name("test item")
                .price(1000)
                .stockQuantity(10)
                .build();
        int orderCount = 11;

        //when //then
        assertThatThrownBy(() -> OrderItem.createOrderItem(item, item.getPrice(), orderCount))
                .isInstanceOf(NotEnoughStockException.class)
                .hasMessage("Not enough stock for item : " + item.getName());
    }

}