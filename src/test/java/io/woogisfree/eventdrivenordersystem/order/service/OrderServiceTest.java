package io.woogisfree.eventdrivenordersystem.order.service;

import io.woogisfree.eventdrivenordersystem.item.domain.Item;
import io.woogisfree.eventdrivenordersystem.item.exception.ItemNotFoundException;
import io.woogisfree.eventdrivenordersystem.item.exception.NotEnoughStockException;
import io.woogisfree.eventdrivenordersystem.item.repository.ItemRepository;
import io.woogisfree.eventdrivenordersystem.member.domain.Member;
import io.woogisfree.eventdrivenordersystem.member.exception.MemberNotFoundException;
import io.woogisfree.eventdrivenordersystem.member.repository.MemberRepository;
import io.woogisfree.eventdrivenordersystem.order.dto.OrderResponse;
import io.woogisfree.eventdrivenordersystem.order.exception.OrderNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static io.woogisfree.eventdrivenordersystem.order.domain.OrderStatus.CANCELLED;
import static io.woogisfree.eventdrivenordersystem.order.dto.CreateOrderRequest.OrderItemRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void beforeEach() {
        memberRepository.deleteAll();
        itemRepository.deleteAll();

        Member member = Member.builder()
                .name("test member")
                .address("test address")
                .build();
        memberRepository.saveAndFlush(member);
        log.info("Initialized Member={}", member);

        Item item = Item.builder()
                .name("test item")
                .price(10000)
                .stockQuantity(30)
                .build();
        itemRepository.saveAndFlush(item);
        log.info("Initialized Item={}", item);
    }

    @DisplayName("주문 생성 시 재고가 정확히 감소하고 주문이 생성된다.")
    @Test
    void orderCreationReducesStockCorrectly() {
        //given
        Item item = itemRepository.findAll().get(0);
        Member member = memberRepository.findAll().get(0);

        OrderItemRequest request = new OrderItemRequest(item.getId(), 10);
        List<OrderItemRequest> orderItems = List.of(request);

        //when
        Long orderId = orderService.createOrder(member.getId(), orderItems);

        //then
        Item updatedItem = itemRepository.findById(item.getId()).orElseThrow();
        assertThat(updatedItem.getStockQuantity()).isEqualTo(20);
        OrderResponse order = orderService.findOrder(orderId);
        assertThat(order.getOrderItems().get(0).getItemId()).isEqualTo(item.getId());
    }


    @DisplayName("멀티스레드 환경에서 주문 생성 시 재고가 정확히 감소하고 주문이 모두 생성된다.")
    @Test
    void concurrentOrderCreationReducesStockCorrectly() throws InterruptedException {
        //given
        Item item = itemRepository.findAll().get(0);
        Member member = memberRepository.findAll().get(0);

        OrderItemRequest request = new OrderItemRequest(item.getId(), 2);
        List<OrderItemRequest> orderItems = List.of(request);

        //when
        ExecutorService executor = Executors.newFixedThreadPool(15);
        List<Callable<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            tasks.add(() -> {
                try {
                    orderService.createOrder(member.getId(), orderItems);
                } catch (Exception e) {
                    log.error("Error in thread {}: {}", Thread.currentThread().getName(), e.getMessage());
                }
                return null;
            });
        }
        List<Future<Void>> results = executor.invokeAll(tasks);
        executor.shutdown();

        for (Future<Void> result : results) {
            try {
                result.get();
            } catch (ExecutionException e) {
                Throwable cause = e.getCause();
                log.error("Thread execution error: {}", cause != null ? cause.getMessage() : "No cause available");
                throw new RuntimeException(e);
            }
        }

        //then
        Item updatedItem = itemRepository.findById(item.getId()).orElseThrow();
        assertThat(updatedItem.getStockQuantity()).isEqualTo(0);

        List<OrderResponse> orders = orderService.findOrdersByMemberId(member.getId());
        assertThat(orders.size()).isEqualTo(15);

        OrderResponse order = orderService.findOrder(orders.get(0).getOrderId());
        assertThat(order.getOrderItems().get(0).getItemId()).isEqualTo(item.getId());
    }

    @DisplayName("존재하지 않는 회원 ID로 주문 생성 시 MemberNotFoundException 예외가 발생한다.")
    @Test
    void shouldThrowMemberNotFoundExceptionWhenOrderingWithNonExistentMemberId() {
        //given
        Long nonExistentMemberId = 999L;
        Item item = itemRepository.findAll().get(0);
        OrderItemRequest request = new OrderItemRequest(item.getId(), 2);
        List<OrderItemRequest> orderItems = List.of(request);

        //when & then
        assertThatThrownBy(() -> orderService.createOrder(nonExistentMemberId, orderItems))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessage("Member with ID " + nonExistentMemberId + " does not exist.");
    }

    @DisplayName("존재하지 않는 상품 ID로 주문 생성 시 ItemNotFoundException 예외가 발생한다.")
    @Test
    void shouldThrowItemNotFoundExceptionWhenOrderingWithNonExistentItemId() {
        //given
        Long nonExistentItemId = 999L;
        Member member = memberRepository.findAll().get(0);
        OrderItemRequest request = new OrderItemRequest(nonExistentItemId, 2);
        List<OrderItemRequest> orderItems = List.of(request);

        //when & then
        assertThatThrownBy(() -> orderService.createOrder(member.getId(), orderItems))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Item with ID " + nonExistentItemId + " does not exist.");
    }

    @DisplayName("재고 수량 초과 시 NotEnoughStockException 예외가 발생한다.")
    @Test
    void shouldThrowNotEnoughStockExceptionWhenOrderExceedsStock() {
        //given
        Item item = itemRepository.findAll().get(0);
        Member member = memberRepository.findAll().get(0);

        OrderItemRequest request = new OrderItemRequest(item.getId(), 31);
        List<OrderItemRequest> orderItems = List.of(request);

        //when & then
        assertThatThrownBy(() -> orderService.createOrder(member.getId(), orderItems))
                .isInstanceOf(NotEnoughStockException.class)
                .hasMessage("Not enough stock for item : " + item.getName());
    }

    @DisplayName("주문 생성 중 예외 발생 시 데이터가 롤백된다.")
    @Test
    void shouldRollbackDataWhenExceptionOccursDuringOrderCreation() {
        //given
        Item item1 = itemRepository.findAll().get(0);
        Member member = memberRepository.findAll().get(0);

        Item item2 = Item.builder()
                .name("test item 2")
                .price(3000)
                .stockQuantity(5)
                .build();
        itemRepository.saveAndFlush(item2);

        List<OrderItemRequest> orderItems = List.of(
                new OrderItemRequest(item1.getId(), 10),
                new OrderItemRequest(item2.getId(), 10) // 재고 초과 -> 예외 발생
        );

        //when
        assertThatThrownBy(() -> orderService.createOrder(member.getId(), orderItems))
                .isInstanceOf(NotEnoughStockException.class)
                .hasMessage("Not enough stock for item : " + item2.getName());

        //then
        Item updatedItem1 = itemRepository.findById(item1.getId()).orElseThrow();
        Item updatedItem2 = itemRepository.findById(item2.getId()).orElseThrow();

        assertThat(updatedItem1.getStockQuantity()).isEqualTo(30);
        assertThat(updatedItem2.getStockQuantity()).isEqualTo(5);

        List<OrderResponse> orders = orderService.findOrdersByMemberId(member.getId());
        assertThat(orders).isEmpty();
    }

    @DisplayName("주문 취소 시 재고가 정확히 증가하고 주문이 취소된다.")
    @Test
    void orderCancellationIncreasesStockCorrectly() {
        //given
        Item item = itemRepository.findAll().get(0);
        Member member = memberRepository.findAll().get(0);

        OrderItemRequest request = new OrderItemRequest(item.getId(), 10);
        List<OrderItemRequest> orderItems = List.of(request);
        Long orderId = orderService.createOrder(member.getId(), orderItems);

        //when
        orderService.cancelOrder(orderId);

        //then
        Item updatedItem = itemRepository.findById(item.getId()).orElseThrow();
        assertThat(updatedItem.getStockQuantity()).isEqualTo(30);
        OrderResponse order = orderService.findOrder(orderId);
        assertThat(order.getStatus()).isEqualTo(CANCELLED);
    }

    @DisplayName("존재하지 않는 주문 ID로 주문 취소 시 OrderNotFoundException 예외가 발생한다.")
    @Test
    void shouldThrowOrderNotFoundExceptionWhenCancellingNonExistentOrderId() {
        //given
        Long nonExistentOrderId = 999L;

        //when & then
        assertThatThrownBy(() -> orderService.cancelOrder(nonExistentOrderId))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessage("Order with ID " + nonExistentOrderId + " does not exist.");
    }

    @DisplayName("특정 주문 ID로 주문을 정상적으로 조회할 수 있다.")
    @Test
    void shouldFindOrderById() {
        //given
        Item item = itemRepository.findAll().get(0);
        Member member = memberRepository.findAll().get(0);

        OrderItemRequest request = new OrderItemRequest(item.getId(), 5);
        List<OrderItemRequest> orderItems = List.of(request);
        Long orderId = orderService.createOrder(member.getId(), orderItems);

        //when
        OrderResponse order = orderService.findOrder(orderId);

        //then
        assertThat(order).isNotNull();
        assertThat(order.getOrderItems()).hasSize(1);
        assertThat(order.getOrderItems().get(0).getItemId()).isEqualTo(item.getId());
    }

    @DisplayName("존재하지 않는 주문 ID로 주문 조회 시 OrderNotFoundException 예외가 발생한다.")
    @Test
    void shouldThrowOrderNotFoundExceptionWhenFindingNonExistentOrderId() {
        //given
        Long nonExistentOrderId = 999L;

        //when & then
        assertThatThrownBy(() -> orderService.findOrder(nonExistentOrderId))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessage("Order with ID " + nonExistentOrderId + " does not exist.");
    }

    @DisplayName("특정 회원 ID로 주문 목록을 정상적으로 조회할 수 있다.")
    @Test
    void shouldFindOrdersByMemberId() {
        //given
        Item item = itemRepository.findAll().get(0);
        Member member = memberRepository.findAll().get(0);

        OrderItemRequest request = new OrderItemRequest(item.getId(), 5);
        List<OrderItemRequest> orderItems = List.of(request);
        orderService.createOrder(member.getId(), orderItems);

        //when
        List<OrderResponse> orders = orderService.findOrdersByMemberId(member.getId());

        //then
        assertThat(orders).isNotEmpty();
        assertThat(orders.get(0).getOrderItems()).hasSize(1);
        assertThat(orders.get(0).getOrderItems().get(0).getItemId()).isEqualTo(item.getId());
    }

    @DisplayName("특정 ID로 주문을 취소할 수 있다.")
    @Test
    void shouldCancelOrderById() {
        //given
        Item item = itemRepository.findAll().get(0);
        Member member = memberRepository.findAll().get(0);

        OrderItemRequest request = new OrderItemRequest(item.getId(), 5);
        List<OrderItemRequest> orderItems = List.of(request);
        Long orderId = orderService.createOrder(member.getId(), orderItems);

        //when
        orderService.deleteOrder(orderId);

        assertThatThrownBy(() -> orderService.findOrder(orderId))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessage("Order with ID " + orderId + " does not exist.");
    }

    @DisplayName("존재하지 않는 주문 ID로 주문 취소 시 OrderNotFoundException 예외가 발생한다.")
    @Test
    void shouldThrowOrderNotFoundExceptionWhenDeletingNonExistentOrderId() {
        //given
        Long nonExistentOrderId = 999L;

        //when & then
        assertThatThrownBy(() -> orderService.deleteOrder(nonExistentOrderId))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessage("Order with ID " + nonExistentOrderId + " does not exist.");
    }
}