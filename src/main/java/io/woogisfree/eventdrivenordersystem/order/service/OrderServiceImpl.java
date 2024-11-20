package io.woogisfree.eventdrivenordersystem.order.service;

import io.woogisfree.eventdrivenordersystem.exception.NotFoundException;
import io.woogisfree.eventdrivenordersystem.item.domain.Item;
import io.woogisfree.eventdrivenordersystem.item.repository.ItemRepository;
import io.woogisfree.eventdrivenordersystem.member.domain.Member;
import io.woogisfree.eventdrivenordersystem.member.repository.MemberRepository;
import io.woogisfree.eventdrivenordersystem.order.domain.Order;
import io.woogisfree.eventdrivenordersystem.order.domain.OrderItem;
import io.woogisfree.eventdrivenordersystem.order.dto.OrderResponse;
import io.woogisfree.eventdrivenordersystem.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public Long createOrder(Long memberId, Long itemId, int count) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("Member with ID " + memberId + " does not exist."));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with ID " + itemId + " does not exist."));
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        Order order = Order.createOrder(member, orderItem);
        orderRepository.save(order);
        return order.getId();
    }

    @Transactional
    @Override
    public void cancelOrder(Long orderId) {
        orderRepository.findById(orderId)
                .ifPresentOrElse(Order::cancel, () -> {
                    throw new NotFoundException("Order with ID " + orderId + " does not exist.");
                });
    }

    @Override
    public Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order with ID " + orderId + " does not exist."));
    }

    @Override
    public List<Order> findOrders(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new NotFoundException("Member with ID " + memberId + " does not exist.");
        }
        return orderRepository.findAllByMemberId(memberId);
    }

    @Transactional
    @Override
    public void deleteOrder(Long orderId) {
        orderRepository.findById(orderId)
                .ifPresentOrElse(order -> {
                    order.cancel();
                    orderRepository.delete(order);
                }, () -> {
                    throw new NotFoundException("Order with ID " + orderId + " does not exist.");
                });
    }

    @Override
    public OrderResponse convertToOrderResponse(Order order) {
        return OrderResponse.builder()
                .orderId(order.getId())
                .memberId(order.getMember().getId())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .orderItems(order.getOrderItems().stream()
                        .map(orderItem -> OrderResponse.OrderItemResponse.builder()
                                .itemId(orderItem.getItem().getId())
                                .itemName(orderItem.getItem().getName())
                                .orderPrice(orderItem.getOrderPrice())
                                .count(orderItem.getCount())
                                .totalPrice(orderItem.getTotalPrice())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
