package io.woogisfree.eventdrivenordersystem.order.service;

import io.woogisfree.eventdrivenordersystem.item.domain.Item;
import io.woogisfree.eventdrivenordersystem.item.exception.ItemNotFoundException;
import io.woogisfree.eventdrivenordersystem.item.repository.ItemRepository;
import io.woogisfree.eventdrivenordersystem.member.domain.Member;
import io.woogisfree.eventdrivenordersystem.member.exception.MemberNotFoundException;
import io.woogisfree.eventdrivenordersystem.member.repository.MemberRepository;
import io.woogisfree.eventdrivenordersystem.order.domain.Order;
import io.woogisfree.eventdrivenordersystem.order.domain.OrderItem;
import io.woogisfree.eventdrivenordersystem.order.dto.OrderResponse;
import io.woogisfree.eventdrivenordersystem.order.exception.OrderNotFoundException;
import io.woogisfree.eventdrivenordersystem.order.mapper.OrderMapper;
import io.woogisfree.eventdrivenordersystem.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final OrderMapper orderMapper;

    @Transactional
    @Override
    public Long createOrder(Long memberId, Long itemId, int count) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member with ID " + memberId + " does not exist."));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Item with ID " + itemId + " does not exist."));
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
                    throw new OrderNotFoundException("Order with ID " + orderId + " does not exist.");
                });
    }

    @Override
    public OrderResponse findOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + orderId + " does not exist."));
        return orderMapper.toOrderResponse(order);
    }

    @Override
    public List<OrderResponse> findOrders(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException("Member with ID " + memberId + " does not exist.");
        }
        List<Order> orders = orderRepository.findAllByMemberId(memberId);
        return orderMapper.toOrderResponseList(orders);
    }

    @Transactional
    @Override
    public void deleteOrder(Long orderId) {
        orderRepository.findById(orderId)
                .ifPresentOrElse(order -> {
                    order.cancel();
                    orderRepository.delete(order);
                }, () -> {
                    throw new OrderNotFoundException("Order with ID " + orderId + " does not exist.");
                });
    }
}
