package com.huyntd.superapp.gundam_shop.repository;

import com.huyntd.superapp.gundam_shop.model.Cart;
import com.huyntd.superapp.gundam_shop.model.CartItem;
import com.huyntd.superapp.gundam_shop.model.Order;
import com.huyntd.superapp.gundam_shop.model.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findAllByCartId(int cartId);
    Optional<CartItem> findByCartIdAndProductId(int cartId, int productId);
}
