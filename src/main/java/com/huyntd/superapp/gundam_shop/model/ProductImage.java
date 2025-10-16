package com.huyntd.superapp.gundam_shop.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "[product_image]")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductImage {

    @Id
    @GeneratedValue
    @Column(name = "id")
    int id;

    @Column(name = "image_url")
    String imageUrl;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;

}
