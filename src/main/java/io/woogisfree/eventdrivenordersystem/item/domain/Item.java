package io.woogisfree.eventdrivenordersystem.item.domain;


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
        if (this.stockQuantity < count) {
            throw new IllegalArgumentException("Not enough stock for item : " + name);
        }
        this.stockQuantity -= count;
    }

    public void addStock(int count) {
        this.stockQuantity += count;
    }
}
