package com.huyntd.superapp.gundam_shop.service.cart;

import com.huyntd.superapp.gundam_shop.dto.request.UpdateCartRequest;
import com.huyntd.superapp.gundam_shop.dto.response.CartResponse;
import com.huyntd.superapp.gundam_shop.model.Cart;
import com.huyntd.superapp.gundam_shop.model.CartItem;

import java.util.Optional;

public interface CartService {

    boolean addToCart(int productId, int userId);
    boolean removeCart(int productId, int userId);
    CartResponse updateCartItem(int userId, UpdateCartRequest updateCart);
    CartResponse getCart(int userId);

}
