package com.huyntd.superapp.gundam_shop.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "[product]")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {

    @Id
    @GeneratedValue
    @Column(name = "id")
    int id;

    @Column(name = "name", length = 100, nullable = false)
    String name;

    @Column(name = "brief_description", length = 500)
    String briefDescription;

    @Column(name = "full_description", length = 1000)
    String fullDescription;

    @Column(name = "technical_specification", length = 500)
    String technicalSpecification;

    @Column(name = "price", nullable = false, precision = 18, scale = 2)
    BigDecimal price;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    List<ProductImage> productImages;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    Category category;

    @Column(name = "quantity", nullable = false)
    int quantity;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    List<CartItem> cartItems;

}
