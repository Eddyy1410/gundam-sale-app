package com.huyntd.superapp.gundam_shop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartRequest {
    private int cartId;
    private List<UpdateCartItemRequest> items;
}
