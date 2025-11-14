package com.huyntd.superapp.gundam_shop.mapper;

import com.huyntd.superapp.gundam_shop.dto.request.UpdateCartItemRequest;
import com.huyntd.superapp.gundam_shop.dto.response.CartItemResponse;
import com.huyntd.superapp.gundam_shop.dto.response.CartResponse;
import com.huyntd.superapp.gundam_shop.dto.response.OrderResponse;
import com.huyntd.superapp.gundam_shop.model.CartItem;
import com.huyntd.superapp.gundam_shop.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "productId", source = "product.id")
    CartItemResponse toCartResponse(CartItem cartItem);
}
