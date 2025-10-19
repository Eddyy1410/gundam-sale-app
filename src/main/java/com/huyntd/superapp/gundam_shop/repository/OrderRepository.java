package com.huyntd.superapp.gundam_shop.repository;

import com.huyntd.superapp.gundam_shop.model.Order;
import com.huyntd.superapp.gundam_shop.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Optional<Page<Order>> findAllByUserId(int userId, Pageable pageable);
    Optional<Page<Order>> findAllByStatus(OrderStatus status, Pageable pageable);
}
