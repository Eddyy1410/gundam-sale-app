package com.huyntd.superapp.gundam_shop.repository;

import com.huyntd.superapp.gundam_shop.model.Order;
import com.huyntd.superapp.gundam_shop.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Optional<Page<Order>> findAllByUserId(int userId, Pageable pageable);
    Optional<Page<Order>> findAllByStatus(OrderStatus status, Pageable pageable);
    Optional<Page<Order>> findAllByStatusAndUserId(OrderStatus status, int userId, Pageable pageable);
    // ------------------ Count orders today ------------------
    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderDate >= :startOfDay AND o.orderDate < :endOfDay")
    long countOrdersToday(@Param("startOfDay") LocalDateTime startOfDay,
                          @Param("endOfDay") LocalDateTime endOfDay);

    // ------------------ Count orders by status ------------------
    long countByStatus(OrderStatus status);
}
