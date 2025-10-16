package com.huyntd.superapp.gundam_shop.repository;

import com.huyntd.superapp.gundam_shop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}
