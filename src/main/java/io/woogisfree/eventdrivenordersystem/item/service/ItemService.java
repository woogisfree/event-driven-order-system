package io.woogisfree.eventdrivenordersystem.item.service;

import io.woogisfree.eventdrivenordersystem.item.dto.ItemResponse;

import java.util.List;

public interface ItemService {

    Long saveItem(String name, int price, int stockQuantity);

    ItemResponse findItem(Long itemId);

    List<ItemResponse> findItems();

    void updateItem(Long itemId, String name, int price, int stockQuantity);

    void deleteItem(Long itemId);

}
