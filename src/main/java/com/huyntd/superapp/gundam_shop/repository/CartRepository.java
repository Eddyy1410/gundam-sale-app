package com.huyntd.superapp.gundam_shop.repository;

import com.huyntd.superapp.gundam_shop.model.Cart;
import com.huyntd.superapp.gundam_shop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    Cart findAllByUserId(int userId);
}
