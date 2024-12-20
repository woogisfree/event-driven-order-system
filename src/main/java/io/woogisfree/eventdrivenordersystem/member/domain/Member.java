package io.woogisfree.eventdrivenordersystem.member.domain;

import io.woogisfree.eventdrivenordersystem.common.BaseEntity;
import io.woogisfree.eventdrivenordersystem.order.domain.Order;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    private String name;
    private String address;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();
//    private List<Order> orders = Collections.synchronizedList(new ArrayList<>());

    @Builder
    private Member(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public void update(String name, String address) {
        this.name = name;
        this.address = address;
    }
}



