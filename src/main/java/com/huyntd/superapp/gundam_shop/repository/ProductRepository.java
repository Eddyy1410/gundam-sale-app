package com.huyntd.superapp.gundam_shop.repository;

import com.huyntd.superapp.gundam_shop.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    @EntityGraph(attributePaths = {"productImages"})
    Page<Product> findByCategory_Id(int categoryId, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"productImages"})
    Page<Product> findAll(Pageable pageable);
}
