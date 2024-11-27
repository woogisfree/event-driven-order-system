package io.woogisfree.eventdrivenordersystem.order.repository;

import io.woogisfree.eventdrivenordersystem.item.domain.Item;
import io.woogisfree.eventdrivenordersystem.item.repository.ItemRepository;
import io.woogisfree.eventdrivenordersystem.member.domain.Member;
import io.woogisfree.eventdrivenordersystem.member.repository.MemberRepository;
import io.woogisfree.eventdrivenordersystem.order.domain.Order;
import io.woogisfree.eventdrivenordersystem.order.domain.OrderItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @DisplayName("회원 아이디로 주문 목록을 조회한다.")
    @Test
    void findAllByMemberId() {
        // given
        Member member = Member.builder()
                .name("member1")
                .address("address1")
                .build();
        memberRepository.save(member);

        Item item1 = Item.builder()
                .name("item1")
                .price(1000)
                .stockQuantity(10)
                .build();
        Item item2 = Item.builder()
                .name("item2")
                .price(2000)
                .stockQuantity(20)
                .build();
        Item item3 = Item.builder()
                .name("item3")
                .price(3000)
                .stockQuantity(30)
                .build();

        itemRepository.saveAll(List.of(item1, item2, item3));

        OrderItem orderItem1 = OrderItem.createOrderItem(item1, item1.getPrice(), 5);
        OrderItem orderItem2 = OrderItem.createOrderItem(item2, item2.getPrice(), 5);
        OrderItem orderItem3 = OrderItem.createOrderItem(item3, item3.getPrice(), 5);
        List<OrderItem> orderItemList = List.of(orderItem1, orderItem2, orderItem3);

        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        // when
        List<Order> orders = orderRepository.findAllByMemberId(member.getId());

        // then
        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getId()).isEqualTo(order.getId());
        assertThat(orders.get(0).getOrderItems()).hasSize(3);
        assertThat(orders.get(0).getOrderItems().contains(orderItem1)).isTrue();
        assertThat(orders.get(0).getOrderItems().contains(orderItem2)).isTrue();
        assertThat(orders.get(0).getOrderItems().contains(orderItem3)).isTrue();
    }

    @DisplayName("회원의 주문 내역이 없는경우 빈 배열을 반환한다.")
    @Test
    void findAllByMemberIdWhenNoOrders() {
        //given
        Member member = Member.builder()
                .name("member1")
                .address("address1")
                .build();
        memberRepository.save(member);

        //when
        List<Order> orders = orderRepository.findAllByMemberId(member.getId());

        //then
        assertThat(orders).isEmpty();
    }


    @DisplayName("주문 아이디로 주문 내역을 조회한다.")
    @Test
    void findOrderWithOrderItemsByOrderId() {

        //given
        Member member = Member.builder()
                .name("member1")
                .address("address1")
                .build();
        memberRepository.save(member);

        Item item1 = Item.builder()
                .name("item1")
                .price(1000)
                .stockQuantity(10)
                .build();
        Item item2 = Item.builder()
                .name("item2")
                .price(2000)
                .stockQuantity(20)
                .build();
        Item item3 = Item.builder()
                .name("item3")
                .price(3000)
                .stockQuantity(30)
                .build();

        itemRepository.saveAll(List.of(item1, item2, item3));

        OrderItem orderItem1 = OrderItem.createOrderItem(item1, item1.getPrice(), 5);
        OrderItem orderItem2 = OrderItem.createOrderItem(item2, item2.getPrice(), 5);
        OrderItem orderItem3 = OrderItem.createOrderItem(item3, item3.getPrice(), 5);
        List<OrderItem> orderItemList = List.of(orderItem1, orderItem2, orderItem3);

        orderItemRepository.saveAll(orderItemList);
        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        //when
        Optional<Order> foundOrder = orderRepository.findOrderWithOrderItemsByOrderId(order.getId());

        //then
        assertThat(order.getId()).isNotNull();
        assertTrue(foundOrder.isPresent());
        Order retrievedOrder = foundOrder.get();
        assertThat(retrievedOrder.getId()).isEqualTo(order.getId());
        assertThat(retrievedOrder.getOrderItems()).hasSize(3);
        assertThat(retrievedOrder.getOrderItems().contains(orderItem1)).isTrue();
        assertThat(retrievedOrder.getOrderItems().contains(orderItem2)).isTrue();
        assertThat(retrievedOrder.getOrderItems().contains(orderItem3)).isTrue();
    }

}