package io.woogisfree.eventdrivenordersystem.item.service;

import io.woogisfree.eventdrivenordersystem.item.domain.Item;

import java.util.List;

public interface ItemService {

    Long saveItem(String name, int price, int stockQuantity);

    Item findItem(Long itemId);

    List<Item> findItems();

    void updateItem(Long itemId, String name, int price, int stockQuantity);

    void deleteItem(Long itemId);

}
