package io.woogisfree.eventdrivenordersystem.item.mapper;

import io.woogisfree.eventdrivenordersystem.item.domain.Item;
import io.woogisfree.eventdrivenordersystem.item.dto.ItemResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemMapper {

    public Item toEntity(ItemResponse dto) {
        if (dto == null) return null;
        return Item.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .stockQuantity(dto.getStockQuantity())
                .build();
    }

    public ItemResponse toDto(Item item) {
        if (item == null) return null;
        return ItemResponse.builder()
                .itemId(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .stockQuantity(item.getStockQuantity())
                .build();
    }

    public List<Item> toEntityList(List<ItemResponse> dtoList) {
        if (dtoList == null) return null;
        return dtoList.stream()
                .map(this::toEntity)
                .toList();
    }

    public List<ItemResponse> toDtoList(List<Item> items) {
        if (items == null) return null;
        return items.stream()
                .map(this::toDto)
                .toList();
    }
}
