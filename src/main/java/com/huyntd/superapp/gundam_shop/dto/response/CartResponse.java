package com.huyntd.superapp.gundam_shop.dto.response;

import com.huyntd.superapp.gundam_shop.dto.request.UpdateCartItemRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartResponse {
    private int userId;
    private int cartId;
    private BigDecimal totalPrice;
    private List<UpdateCartItemRequest> items;
}
