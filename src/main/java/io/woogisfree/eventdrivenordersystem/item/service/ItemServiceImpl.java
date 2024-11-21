package io.woogisfree.eventdrivenordersystem.item.service;

import io.woogisfree.eventdrivenordersystem.exception.NotFoundException;
import io.woogisfree.eventdrivenordersystem.item.domain.Item;
import io.woogisfree.eventdrivenordersystem.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public Long saveItem(String name, int price, int stockQuantity) {
        Item item = Item.builder()
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .build();
        return itemRepository.save(item).getId();
    }

    @Override
    public Item findItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with ID " + itemId + " does not exist."));
    }

    @Override
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    @Transactional
    @Override
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with ID " + itemId + " does not exist."));
        item.update(name, price, stockQuantity);
    }

    // TODO: 주문 목록에 해당 상품이 존재하는지 확인하는 로직 추가
    @Transactional
    @Override
    public void deleteItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with ID " + itemId + " does not exist."));
        itemRepository.delete(item);
    }
}
