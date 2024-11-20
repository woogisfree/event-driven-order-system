package io.woogisfree.eventdrivenordersystem.item.domain;


import io.woogisfree.eventdrivenordersystem.exception.NotEnoughStockException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    public Item(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public void reduceStock(int count) {
        int restStock = this.stockQuantity - count;
        if (restStock < 0) {
            throw new NotEnoughStockException("Not enough stock for item : " + name);
        }
        this.stockQuantity = restStock;
    }

    public void addStock(int count) {
        this.stockQuantity += count;
    }
}
