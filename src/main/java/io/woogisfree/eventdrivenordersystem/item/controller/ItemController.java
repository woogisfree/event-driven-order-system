package io.woogisfree.eventdrivenordersystem.item.controller;

import io.woogisfree.eventdrivenordersystem.common.ApiResponse;
import io.woogisfree.eventdrivenordersystem.item.domain.Item;
import io.woogisfree.eventdrivenordersystem.item.dto.ItemResponse;
import io.woogisfree.eventdrivenordersystem.item.dto.SaveItemRequest;
import io.woogisfree.eventdrivenordersystem.item.dto.UpdateItemRequest;
import io.woogisfree.eventdrivenordersystem.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/items")
@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> saveItem(@RequestBody SaveItemRequest request) {
        Long itemId = itemService.saveItem(request.getName(), request.getPrice(), request.getStockQuantity());
        return new ResponseEntity<>(ApiResponse.success(itemId, "Item created successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ApiResponse<ItemResponse>> findItem(@PathVariable("itemId") Long itemId) {
        Item item = itemService.findItem(itemId);
        ItemResponse response = ItemResponse.builder()
                .itemId(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .stockQuantity(item.getStockQuantity())
                .build();
        return ResponseEntity.ok(ApiResponse.success(response, "Item found successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ItemResponse>>> findItems() {
        List<Item> items = itemService.findItems();
        List<ItemResponse> response = items.stream()
                .map(item -> ItemResponse.builder()
                        .itemId(item.getId())
                        .name(item.getName())
                        .price(item.getPrice())
                        .stockQuantity(item.getStockQuantity())
                        .build())
                .toList();
        return ResponseEntity.ok(ApiResponse.success(response, "Items found successfully"));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ApiResponse<Void>> updateItem(@PathVariable("itemId") Long itemId, @RequestBody UpdateItemRequest request) {
        itemService.updateItem(itemId, request.getName(), request.getPrice(), request.getStockQuantity());
        return new ResponseEntity<>(ApiResponse.success(null, "Item updated successfully"), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable("itemId") Long itemId) {
        itemService.deleteItem(itemId);
        return new ResponseEntity<>(ApiResponse.success(null, "Item deleted successfully"), HttpStatus.NO_CONTENT);
    }
}
