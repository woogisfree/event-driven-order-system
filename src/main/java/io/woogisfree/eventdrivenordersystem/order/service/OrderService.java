package io.woogisfree.eventdrivenordersystem.order.service;

import io.woogisfree.eventdrivenordersystem.item.domain.Item;
import io.woogisfree.eventdrivenordersystem.item.exception.ItemNotFoundException;
import io.woogisfree.eventdrivenordersystem.item.repository.ItemRepository;
import io.woogisfree.eventdrivenordersystem.member.domain.Member;
import io.woogisfree.eventdrivenordersystem.member.exception.MemberNotFoundException;
import io.woogisfree.eventdrivenordersystem.member.repository.MemberRepository;
import io.woogisfree.eventdrivenordersystem.order.domain.Order;
import io.woogisfree.eventdrivenordersystem.order.domain.OrderItem;
import io.woogisfree.eventdrivenordersystem.order.dto.CreateOrderRequest;
import io.woogisfree.eventdrivenordersystem.order.dto.OrderResponse;
import io.woogisfree.eventdrivenordersystem.order.exception.OrderNotFoundException;
import io.woogisfree.eventdrivenordersystem.order.mapper.OrderMapper;
import io.woogisfree.eventdrivenordersystem.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class OrderService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public Long createOrder(Long memberId, List<CreateOrderRequest.OrderItemRequest> orderItems) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member with ID " + memberId + " does not exist."));

        List<OrderItem> orderItemList = new ArrayList<>();
        for (CreateOrderRequest.OrderItemRequest itemRequest : orderItems) {
            Item item = itemRepository.findById(itemRequest.getItemId())
                    .orElseThrow(() -> new ItemNotFoundException("Item with ID " + itemRequest.getItemId() + " does not exist."));

            OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), itemRequest.getCount());
            orderItemList.add(orderItem);
        }
        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);
        return order.getId();
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        orderRepository.findOrderWithOrderItemsByOrderId(orderId)
                .ifPresentOrElse(Order::cancel, () -> {
                    throw new OrderNotFoundException("Order with ID " + orderId + " does not exist.");
                });
    }

    public OrderResponse findOrder(Long orderId) {
        Order order = orderRepository.findOrderWithOrderItemsByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + orderId + " does not exist."));
        return orderMapper.toOrderResponse(order);
    }

    public List<OrderResponse> findOrders(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException("Member with ID " + memberId + " does not exist.");
        }
        List<Order> orders = orderRepository.findAllByMemberId(memberId);
        return orderMapper.toOrderResponseList(orders);
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        orderRepository.findOrderWithOrderItemsByOrderId(orderId)
                .ifPresentOrElse(order -> {
                    order.cancel();
                    orderRepository.delete(order);
                }, () -> {
                    throw new OrderNotFoundException("Order with ID " + orderId + " does not exist.");
                });
    }
}
