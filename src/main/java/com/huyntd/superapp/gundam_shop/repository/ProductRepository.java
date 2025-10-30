package com.huyntd.superapp.gundam_shop.repository;

import com.huyntd.superapp.gundam_shop.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    @EntityGraph(attributePaths = {"productImages"})
    Page<Product> findByCategory_Id(int categoryId, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"productImages"})
    Page<Product> findAll(Pageable pageable);

    // 1. Tự động sinh query dựa trên tên phương thức
    List<Product> findByQuantityLessThanEqual(int threshold);

    // 2. Dùng @Query tùy chỉnh nếu muốn
    @Query("SELECT p FROM Product p WHERE p.quantity <= :threshold ORDER BY p.quantity ASC")
    List<Product> findLowStockProducts(@Param("threshold") int threshold);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.quantity <= :threshold")
    long countLowStock(int threshold);
}
