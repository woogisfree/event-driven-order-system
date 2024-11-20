package io.woogisfree.eventdrivenordersystem.item.repository;

import io.woogisfree.eventdrivenordersystem.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

}
