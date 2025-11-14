package com.huyntd.superapp.gundam_shop.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemResponse {
    private int productId;
    private String productName;
    private String productImage;
    private double productPrice;
    private int quantity;
}

