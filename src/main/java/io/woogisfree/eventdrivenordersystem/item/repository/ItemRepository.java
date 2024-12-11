package io.woogisfree.eventdrivenordersystem.item.repository;

import io.woogisfree.eventdrivenordersystem.item.domain.Item;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i WHERE i.id = :id")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Item> findByIdWithLock(@Param("id") Long id);

}
