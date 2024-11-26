package io.woogisfree.eventdrivenordersystem.item.service;

import io.woogisfree.eventdrivenordersystem.item.domain.Item;
import io.woogisfree.eventdrivenordersystem.item.dto.ItemResponse;
import io.woogisfree.eventdrivenordersystem.item.exception.ItemNotFoundException;
import io.woogisfree.eventdrivenordersystem.item.mapper.ItemMapper;
import io.woogisfree.eventdrivenordersystem.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Transactional
    public Long saveItem(String name, int price, int stockQuantity) {
        Item item = Item.builder()
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .build();
        return itemRepository.save(item).getId();
    }

    public ItemResponse findItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Item with ID " + itemId + " does not exist."));
        return itemMapper.toDto(item);
    }

    public List<ItemResponse> findItems() {
        List<Item> items = itemRepository.findAll();
        return itemMapper.toDtoList(items);
    }

    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Item with ID " + itemId + " does not exist."));
        item.update(name, price, stockQuantity);
    }

    // TODO: 주문 목록에 해당 상품이 존재하는지 확인하는 로직 추가
    @Transactional
    public void deleteItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Item with ID " + itemId + " does not exist."));
        itemRepository.delete(item);
    }

}
